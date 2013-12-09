package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.dylan.Agent7.VulnerabilityData;
import me.dylan.Agent7.gui.FrameResult;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Fuzzer for Error-Based SQL Injection exploits
 * 
 * @author Dylan
 * 
 */
public class FuzzerSQL extends Fuzzer implements Injector {

	public FuzzerSQL(String url) {
		if (!url.startsWith("htt"))
			url = "http://" + url;
		this.setUrl(url);
		try {
			payloads = PayloadUtil.getInjectionPayloads("SQLTests.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void initializeAttack() {
		this.sendInitialRequest();
		this.gatherAllFormIds();
		this.beginInjection();
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

	@Override
	public void beginInjectionForms() {
		for (Element e : forms) {
			connectionMethod = e.attr("method");
			if (e.hasAttr("abs:action"))
				setUrl(e.attr("abs:action"));

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
	public void beginInjection() {
		info("Beginning SQL injection process.");
		info("Testing forms...");
		beginInjectionForms();
		info("Finished!");
	}

	@Override
	public void executeTestConnection(ArrayList<String> params) {
		for (String name : params) {
			for (String payload : payloads) {
				try {
					int index = payloads.indexOf(payload);
					Connection connection = Fuzzer.getConnection(getUrl());
					sendGetPostPayloads(connection, payload);
					verifyPayloadExecution(index, name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void verifyPayloadExecution(int index, String name)
			throws UnknownHostException {
		if (doc.html().toLowerCase()
				.contains("you have an error in your sql syntax")
				|| doc.html().toLowerCase()
						.contains("sql command not properly ended")
				|| doc.html()
						.toLowerCase()
						.contains(
								"unclosed quotation mark after the character string")
				|| doc.html()
						.toLowerCase()
						.contains(
								"query failed: error: syntax error at or near"))
			FrameResult.urgent(new VulnerabilityData(this.getFriendlyName(),
					payloads.get(index), name, getUrl(), connectionMethod));
	}

	@Override
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

	@Override
	public String getFriendlyName() {
		return "SQL Injector";
	}

}
