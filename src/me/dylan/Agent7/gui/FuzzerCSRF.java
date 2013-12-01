package me.dylan.Agent7.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.nodes.Element;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.http.Fuzzer.Fuzzer;
import me.dylan.Agent7.http.Fuzzer.IFuzzer;
import me.dylan.Agent7.http.Fuzzer.PayloadUtil;

public class FuzzerCSRF extends Fuzzer {

	public FuzzerCSRF(String url) {
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
		this.verifyPayloadExecution();
	}

	@Override
	public void executeTestConnection(ArrayList<String> params) {}

	@Override
	public void sendGetPostPayloads(Connection connection, String payload) {}

	public void verifyPayloadExecution() {
		for (Element e : forms) {
			if (!e.hasAttr("method") || e.attr("method").toLowerCase() == "get") {
				Agent7.logLine("Warning: Possible vulnerability on form, id: "
						+ e.id() + " type: CSRF - Cross-site Request Forgery");
			}
		}
	}
}
