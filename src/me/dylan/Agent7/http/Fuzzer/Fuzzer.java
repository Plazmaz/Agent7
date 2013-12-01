package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class Fuzzer implements IFuzzer {
	protected ArrayList<Element> forms = new ArrayList<Element>();
	protected ArrayList<String> payloads = new ArrayList<String>();
	ArrayList<String> params = new ArrayList<String>();
	Document doc = null;
	private String url = "";
	String connectionMethod = "";

	public void gatherAllFormIds() {
		Agent7.logLine("Searching for possibly vunerable form inputs...");
		for (Element e : doc.getAllElements()) {
			if (e.className().contains("form") || e.hasAttr("method")
					|| e.hasAttr("action")) {
				forms.add(e);
			}
		}
	}

	public void sendInitialRequest() {
		Agent7.logLine("Retrieving initial webpage data.");
		try {
			doc = getConnection(getUrl()).get();
		} catch (IOException e) {
			Agent7.err(e);
		}
	}

	public static Connection getConnection(String url) throws UnknownHostException {
		Connection connection = Jsoup.connect(url);
		if (Agent7.useCookies)
			connection.cookies(Agent7.cookies);
		connection.timeout(10000);
		connection.followRedirects(true);
		connection
				.userAgent("Agent7 - if you did not initiate this penetration test, "
						+ "here's my ip: " + Inet4Address.getLocalHost());
		return connection;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
