package me.dylan.Agent7.http.Fuzzer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.http.HTTPUtil;

public class FuzzerSQLBlind extends FuzzerSQL {

	public FuzzerSQLBlind(String url) {
		super(url);
		try {
			payloads = PayloadUtil.getInjectionPayloads("BlindSQLTests.txt");
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

	@Override
	public void executeTestConnection(ArrayList<String> params) {
		for (String name : params) {
			for (String payload : payloads) {
				try {
					int index = payloads.indexOf(payload);
					Connection connection = Fuzzer.getConnection(getUrl());
					sendGetPostPayloads(connection, payload);
					Agent7.logLine("Sent request to " + getUrl() + " Method: "
							+ connectionMethod +" Payload: "+payload);
					verifyPayloadExecution(index, name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sendGetPostPayloads(Connection connection, String payload) {
		for (String param : params) {
			if (param.isEmpty())
				continue;
			connection.data(param, payload);
			try {
				if (connectionMethod.equalsIgnoreCase("post")) {
					Connection.Response response = connection.method(
							Method.POST).execute();
					doc = response.parse();
				} else {
					Connection.Response response = connection
							.method(Method.GET).execute();
					doc = response.parse();
				}
			} catch (SocketTimeoutException e) {
				Agent7.logLine("Found blind sql injection exploit with payload: "
						+ payload + " On form: " + param);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void beginInjection() {
		Agent7.logLine("Beginning blind SQL injection process.");
		Agent7.logLine("Testing forms...");
		beginInjectionForms();
		Agent7.logLine("Finished!");
	}

	@Override
	public void verifyPayloadExecution(int index, String name) {
		try {
			HTTPUtil.sendHTTPRequest(getUrl());
		} catch (IOException e) {
			if (e instanceof MalformedURLException)
				Agent7.err(e);
			else
				Agent7.logLine("Found blind sql injection exploit with payload: "
						+ payloads.get(index) + " On form: " + name);
		}

	}

}
