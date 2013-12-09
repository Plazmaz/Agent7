package me.dylan.Agent7.http.Fuzzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.VulnerabilityData;
import me.dylan.Agent7.gui.FrameMain;
import me.dylan.Agent7.gui.FrameResult;
import me.dylan.Agent7.res.ContentLoader;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author Dylan
 * 
 */
public class FuzzerPhp extends Fuzzer implements Injector {

	public FuzzerPhp(String url) {
		if (!url.startsWith("htt"))
			url = "http://" + url;
		this.setUrl(url);
		try {
			payloads = PayloadUtil.getInjectionPayloads("PHPTests.txt");
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
		// Elements linkElements = doc.getElementsByAttribute("href");
		try {
			params.addAll(getCommonURLExtensions());
		} catch (IOException e) {
			e.printStackTrace();
		}

		info("Testing popular url extensions(GET).");
		connectionMethod = "GET";
		executeTestConnection(params);

		info("Testing popular url extensions(POST).");
		connectionMethod = "POST";
		executeTestConnection(params);

	}

	@Override
	public void beginInjectionForms() {

		for (Element e : forms) {
			setUrl(e.attr("abs:action"));
			connectionMethod = e.attr("method");
			if (getUrl().isEmpty())
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

	public static ArrayList<String> getCommonURLExtensions() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				ContentLoader.getInternalFileStream("OWASPDIR.TXT")));
		String s = "";
		ArrayList<String> tmp = new ArrayList<String>();
		while ((s = in.readLine()) != null) {
			if (!s.startsWith("#") && !s.isEmpty())
				tmp.add(s);
		}
		return tmp;
	}

	@Override
	public void beginInjection() {
		info("Beginning PHP injection process.");
		info("Testing forms...");
		beginInjectionForms();
		info("Finished!");

		info("Testing extras...");
		beginInjectionLinks();
		info("Finished!");
	}

	@Override
	public void executeTestConnection(ArrayList<String> params) {
		FrameMain.setTask("Injecting...");
		int queriesAttempted = 0;
		for (String name : params) {
			for (String payload : payloads) {
				FrameMain.setTask("Injecting... Current param: \"" + name
						+ "\", payload:\"" + payload + "\"");
				int index = payloads.indexOf(payload);
				queriesAttempted++;
				double progress = ((double) (queriesAttempted) / (double) ((payloads
						.size() * params.size()))) * 100;
				Agent7.setProgress((int) Math.ceil(progress));

				try {
					payload = payload.replace("#ip", InetAddress.getLocalHost()
							.getHostAddress());
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				payload = payload.replace("#payload", "vulnerablePage" + index
						+ "#" + name);
				// payload = payload.replace("#testfile",
				// "http://www.mediafire.com/?3akbzhyfo9827nr");
				try {
					Connection connection = Fuzzer.getConnection(getUrl());
					sendGetPostPayloads(connection, payload);
					verifyPayloadExecution(index, name);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		FrameMain.setTask("Awaiting commands...");
	}

	public void verifyPayloadExecution(int index, String name)
			throws UnknownHostException {
		Element e = doc.getElementById("vulnerablePage" + index + "#" + name);
		if (e != null
				&& e.data().contains(
						InetAddress.getLocalHost().getHostAddress())) {
			FrameResult.urgent(new VulnerabilityData(this.getFriendlyName(),
					payloads.get(index), name, getUrl(), connectionMethod));
		}
	}

	@Override
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public String getFriendlyName() {
		return "PHP Injector";
	}
}
