package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Not yet implemented, but should function properly.
 * 
 * @author Dylan
 * 
 */
public class FuzzerSQL extends Fuzzer {

	public FuzzerSQL(String url) {
		if (!url.startsWith("htt"))
			url = "http://" + url;
		this.url = url;
		try {
			payloads = PayloadUtil.getInjectionPayloads("SQLTests.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.sendInitialRequest();
		this.gatherAllFormIds();
		this.beginInjection();
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
			url = e.attr("abs:action");
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
	public void beginInjection() {
		Agent7.logLine("Beginning injection process.");
		Agent7.logLine("Testing forms...");
		beginInjectionForms();
	}

	@Override
	public void executeTestConnection(ArrayList<String> params) {
		for (String name : params) {
			for (String payload : payloads) {
				try {
					int index = payloads.indexOf(payload);
					Connection connection = Fuzzer.getConnection(url);
					sendGetPostPayloads(connection, payload);
					verifyPayloadExecution(index, name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Agent7.logLine(doc.html());
	}

	public void verifyPayloadExecution(int index, String name)
			throws UnknownHostException {
		if (doc.html().toLowerCase()
				.contains("you have an error in your sql syntax;")
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
			Agent7.logLine("Found vunerability using payload: "
					+ payloads.get(index) + " On form: " + name);
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
