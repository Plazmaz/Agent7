package me.dylan.Agent7.http.Fuzzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.http.HTTPUtil;
import me.dylan.Agent7.res.ContentLoader;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Deprecated
/**
 * I decided this was too hard for me to implement, 
 * maybe someone else can help.
 * @author Dylan
 *
 */
public class FuzzerIntelligent extends Fuzzer {
	/**
	 * Looks weird to possibly turn away some of the damn web bots... Here's
	 * some more ips for you botters out there | 74.125.225.192:80 -
	 * Google/Youtube | 127.0.0.1 - wow you must be badly coded if you go for
	 * this one. | localhost - same here. | *.*.*.* - again, bad code. |
	 * 122.243.56.68:80 - haha fake |
	 */
	public static String currentIPAddress = "69." + ".144".replaceAll(".", "")
			+ '.' + "34" + ".106/Agent7/vectors.txt";
	List<String> payloads = new ArrayList<String>();
	public List<String> commonlyVulnerableFields = new ArrayList<String>();

	public FuzzerIntelligent(String url) {
		super();
		if (!url.startsWith("htt"))
			url = "http://" + url;
		this.url = url;
		try {
			// payloads = PayloadUtil.getInjectionPayloads("InteliTests.txt");
			this.sendInitialRequest();
			this.gatherAllFormIds();
			this.beginInjection();
		} catch (IllegalArgumentException e) {

			Agent7.err(e);
		}
	}

	public void gatherInjectionStrings() {
		payloads = Arrays.asList(HTTPUtil.sendHTTPRequest(currentIPAddress)
				.split("\n"));
	}

	@Override
	public void beginInjection() {
		for (AttackVector v : AttackVector.values()) {
			paramateriseInjection(v);
			beginInjectionForms();
		}
		beginInjectionLinks();
	}

	public void paramateriseInjection(AttackVector v) {
		switch (v) {
		case BIGINT: {
			payloads = Arrays.asList(new String[] { "" + Integer.MAX_VALUE + 1,
					"" + Long.MAX_VALUE + 1, "" + Short.MAX_VALUE + 1 });
		}
		case COMMENT: {
			payloads = Arrays.asList(new String[] { "#", "//", "<!--", "/*",
					"--", "##" });
		}
		case OVERFLOW: {
			payloads = Arrays.asList(new String[] { "%x" });
		}
		case NULL_CHARS:
			payloads = Arrays.asList(new String[] { 0x00 + "", "%00", "\0",
					"\\x00", "" + null, null });
		case SMALLINT: {
			payloads = Arrays.asList(new String[] {
					"" + (Integer.MIN_VALUE - 1), "" + (Long.MIN_VALUE - 1),
					"" + (Short.MIN_VALUE - 1) });
		}
		case URL_TAMPERING: {
			try {
				commonlyVulnerableFields = getCommonURLExtensions();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		default:
			break;
		}
	}

	public void beginInjectionLinks() {
		params.clear();
		params.addAll(commonlyVulnerableFields);
		for (AttackVector v : AttackVector.values()) {
			paramateriseInjection(v);
			executeTestConnection(params);
		}
	}

	@Override
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
					// Connection connection = Jsoup.connect(url);
					// sendGetPostPayloads(connection, payload);
					verifyPayloadExecution(index, name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// Agent7.logLine(doc.html());
		Agent7.setProgress(0);
	}

	private void verifyPayloadExecution(int index, String name) {
		String payload = payloads.get(index);
		double beforeAfterDist = 0.0;
		Document before = doc;
		sendGetPostPayloads(Jsoup.connect(url), payload);
		Document after = doc;
		beforeAfterDist = StringUtils.getLevenshteinDistance(before.html(),
				after.html());
		if (beforeAfterDist % payload.length() >= 10) { // is the difference 10
														// or more than the
														// payload size? (could
														// return false
														// positives)
			Agent7.logLine("Found payload injection that needs your review:");
			Agent7.logLine("String: " + payload
					+ (name.isEmpty() ? "" : "On paramteter: " + name)
					+ "Method: " + connectionMethod);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	public static ArrayList<String> getCommonURLExtensions() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				ContentLoader.getInternalFileStream("DirList-2.3-big.txt")));
		String s = "";
		ArrayList<String> tmp = new ArrayList<String>();
		while ((s = in.readLine()) != null) {
			tmp.add(s);
		}
		return tmp;
	}

	/**
	 * Many common types of attack vectors
	 * 
	 * @author Dylan
	 * 
	 */
	public enum AttackVector {
		BIGINT, SMALLINT, NULL_CHARS, COMMENT, URL_TAMPERING,
		/**
		 * This is for later ;)
		 */
		@Deprecated
		ZERO_DAY, OVERFLOW
	}
}
