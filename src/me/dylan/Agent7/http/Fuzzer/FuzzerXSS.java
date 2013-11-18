package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FuzzerXSS extends Fuzzer {

	public FuzzerXSS(String url) {
		if (!url.startsWith("htt"))
			url = "http://" + url;
		this.url = url;
		try {
			payloads = PayloadUtil.getInjectionPayloads("XSSTests.txt");
			this.sendInitialRequest();
			this.gatherAllFormIds();
			this.beginInjection();
		} catch (IllegalArgumentException e) {

			Agent7.err(e);
		} catch (IOException e) {
			Agent7.err(e);
		}
	}

	@Override
	public void beginInjection() {
		Agent7.logLine("Beginning injection process.");
		Agent7.logLine("Testing forms...");
		beginInjectionForms();
		// Agent7.logLine("Testing links...");
		// beginInjectionLinks();
	}

	public void beginInjectionLinks() {

		Elements linkElements = doc.getElementsByTag("a");
		for (Element inputEl : linkElements) {
			if (!inputEl.text().contains(url))
				continue;
			url = inputEl.attr("abs:href");
			connectionMethod = "GET";
			params.add(inputEl.text());
		}
		executeTestConnection(params);
	}

	public void beginInjectionForms() {
		for (Element e : forms) {
			if (url == null || url.isEmpty())
				Agent7.logLine("Could not find url, using default: " + url);
			url = e.attr("abs:action");
			// else
			connectionMethod = e.attr("method");
			if (url.isEmpty())
				continue;
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
					Connection connection = Jsoup.connect(url);
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
			Agent7.logLine("Found " + (saved ? "stored" : "local")
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
