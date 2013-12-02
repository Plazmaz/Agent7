package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 
 * Fuzz for Cross-Site Scripting exploits
 * @author Dylan
 *
 */
public class FuzzerXSS extends Fuzzer implements Injector {

	public FuzzerXSS(String url) {
		if (!url.startsWith("htt"))
			url = "http://" + url;
		this.setUrl(url);
		try {
			payloads = PayloadUtil.getInjectionPayloads("XSSTests.txt");
		} catch (IllegalArgumentException e) {

			Agent7.err(e);
		} catch (IOException e) {
			Agent7.err(e);
		}
	}
	public void initializeAttack() {
		this.sendInitialRequest();
		this.gatherAllFormIds();
		this.beginInjection();
	}

	@Override
	public void beginInjection() {
		Agent7.logLine("Beginning script injection process.");
		Agent7.logLine("Testing forms...");
		beginInjectionForms();
		Agent7.logLine("Finished!");
		// Agent7.logLine("Testing links...");
		// beginInjectionLinks();
	}

	public void beginInjectionLinks() {

		Elements linkElements = doc.getElementsByTag("a");
		for (Element inputEl : linkElements) {
			if (!inputEl.text().contains(getUrl()))
				continue;
			setUrl(inputEl.attr("abs:href"));
			connectionMethod = "GET";
			params.add(inputEl.text());
		}
		executeTestConnection(params);
	}

	public void beginInjectionForms() {
		for (Element e : forms) {
			if (!e.hasAttr("action"))
				Agent7.logLine("Could not find url, using default: " + getUrl());
			else
				setUrl(e.attr("abs:action"));
			// else
			connectionMethod = e.attr("method");
			if (connectionMethod.isEmpty())
				connectionMethod = "GET";
			Elements inputElements = e.getElementsByTag("input");
			for (Element inputEl : inputElements) {
				if (inputEl.attr("type").toLowerCase() == "submit")
					continue;
				params.add(inputEl.attr("name"));
			}
			executeTestConnection(params);
		}
	}

	@Override
	public void executeTestConnection(ArrayList<String> params) {
		int queriesAttempted = 0;
		for (String name : params) {
			for (String payload : payloads) {
				try {
					int index = payloads.indexOf(payload);
					queriesAttempted++;
					double progress = ((double) (queriesAttempted) / (double) ((payloads
							.size() * params.size()))) * 100;
					Agent7.setProgress((int) Math.ceil(progress));

					payload = payload.replace("#ip", Inet4Address
							.getLocalHost().getHostAddress());
					payload = payload.replace("#payload", "vulnerablePage"
							+ index + "#" + name);
					Connection connection = Fuzzer.getConnection(getUrl());
					verifyPayloadExecution(index, name, true);
					sendGetPostPayloads(connection, payload);
					verifyPayloadExecution(index, name, false); // not confirmed
																// functioning.
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Agent7.logLine(doc.html());
		Agent7.setProgress(0);
	}

	/**
	 * Did our payload execute?
	 * 
	 * @param index
	 *            - The index of the payload in the document
	 * @param name
	 *            - the name of the possibly vulnerable form
	 * @param saved
	 *            - do we check for stored or local xss?
	 * @throws UnknownHostException
	 */
	public void verifyPayloadExecution(int index, String name, boolean saved)
			throws UnknownHostException {
		Element e = doc.getElementById("vulnerablePage" + index + "#" + name);
		if (e != null
				&& !e.parents().html().contains("noscript")
				&& e.data().contains(
						Inet4Address.getLocalHost().getHostAddress()))
			warning("Found " + (saved ? "stored" : "local")
					+ " vunerability using payload: " + payloads.get(index)
					+ " On form: " + name);
	}

	public void sendGetPostPayloads(Connection connection, String payload) {
		for (String param : params) {
			if (param.isEmpty())
				continue;
			connection.data(param, payload);
		}
		try {
			if (connectionMethod.equalsIgnoreCase("post")) {
				Connection.Response response = connection.method(Method.POST)
						.execute();
				doc = response.parse();
			} else {
				Connection.Response response = connection.method(Method.GET)
						.execute();
				doc = response.parse();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
