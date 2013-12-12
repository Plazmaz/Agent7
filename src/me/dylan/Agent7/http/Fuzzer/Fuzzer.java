package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.gui.FrameFuzzer;

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

	@Override
	public void gatherAllFormIds() {
		info("Searching for possibly vunerable form inputs...");
		for (Element e : doc.getAllElements()) {
			if (e.className().contains("form") || e.hasAttr("method")
					|| e.hasAttr("action")) {
				forms.add(e);
			}
		}
	}

	@Override
	public void sendInitialRequest() {
		info("Retrieving webpage data from site " + getUrl());
		try {
			doc = getConnection(getUrl()).get();
		} catch (IOException e) {
			err(e);
		}
	}

	public static Connection getConnection(String url)
			throws UnknownHostException {
		Connection connection = Jsoup.connect(url);
		if (Agent7.useCookies)
			connection.cookies(Agent7.cookies);
		connection.timeout(8000);
		connection.followRedirects(true);
		if (FrameFuzzer.useragent.getText().isEmpty()) {
			connection
					.userAgent("Agent7 - if you did not initiate this penetration test, "
							+ "here's my ip: " + InetAddress.getLocalHost());
		} else {
			connection.userAgent(FrameFuzzer.useragent.getText());
		}
		return connection;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void info(String info) {
		Agent7.logLine("[" + this.getFriendlyName() + "] " + info);
	}

	@Override
	public void err(Exception e) {
		Agent7.logLine("[" + this.getFriendlyName() + "] An error occured: "
				+ e.getMessage());
	}

	public String getFriendlyName() {
		return "Fuzzer Generic";
	}

}
