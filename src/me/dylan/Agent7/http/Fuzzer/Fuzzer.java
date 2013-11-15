package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Fuzzer {
	ArrayList<Element> forms = new ArrayList<Element>();
	ArrayList<String> payloads = new ArrayList<String>();
	ArrayList<String> params = new ArrayList<String>();
	Document doc = null;
	String url = "";
	String connectionMethod = "";

	public void gatherAllFormIds() {
		Agent7.logLine("Searching for possibly vunerable inputs...");
		for (Element e : doc.getAllElements()) {
			if (e.classNames().contains("form") || e.hasAttr("method") || e.hasAttr("action")) {
				forms.add(e);
			}
		}
	}

	public void sendInitialRequest() {
		Agent7.logLine("Retrieving initial webpage data.");
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void beginInjection() {}

	public void beginInjectionForms() {}

	public void executeTestConnection(ArrayList<String> params) {}

	public void sendGetPostPayloads(Connection connection, String payload) {}
	
}
