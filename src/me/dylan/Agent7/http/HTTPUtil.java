package me.dylan.Agent7.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadSyncCrawler;
import me.dylan.Agent7.gui.FrameFuzzer;

public class HTTPUtil {
	static String charset = "UTF-8";

	public static String sendHTTPRequest(String url) throws IOException {
		URL destination = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) destination
				.openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		if (FrameFuzzer.useragent.getText().isEmpty()) {
			connection.setRequestProperty("User-agent",
					"Agent7 - if you did not initiate this penetration test, "
							+ "here's my ip: "
							+ InetAddress.getLocalHost().getHostAddress());
		} else {
			connection.setRequestProperty("User-agent",
					FrameFuzzer.useragent.getText());
		}
		// Launched Nov 1st, 2013
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=" + charset);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write("v=Agent7_v"+Agent7.version);
        wr.write("ip="+InetAddress.getLocalHost().getAddress());
		BufferedReader data = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String input = "";
		String curLine;
		for (String key : connection.getHeaderFields().keySet()) {
			input += connection.getHeaderField(key);
		}
		while ((curLine = data.readLine()) != null) {
			input += curLine;
		}

		return input;
	}

	public static String sendHTTPPing(String url) throws IOException {
		URL destination = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) destination.openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		// Yes, We're lying to the server. This is because our current host
		// doesn't like
		// custom user strings.
		connection
				.setRequestProperty("user-agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=" + charset);

		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write("v=Agent7_v"+Agent7.version);
        wr.write("ip="+InetAddress.getLocalHost().getAddress());
		BufferedReader data = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String input = "";
		String curLine;
		// for (String key : connection.getHeaderFields().keySet()) {
		// input += connection.getHeaderField(key);
		// }
		while ((curLine = data.readLine()) != null) {
			input += curLine;
		}
		return input;
	}

	public static ArrayList<String> getDisallowedFromRobots(String baseurl,
			ArrayList<String> robots) {
		ArrayList<String> disallowed = new ArrayList<String>();
		for (String s : robots) {
			if (s.startsWith("Disallow: ")) {
				disallowed.add(s.replace("Disallow: ", ""));
			}
		}
		return disallowed;
	}
	//This is perhaps for later.
//	public static ArrayList<String> readRobots(String base) throws IOException {
//		String url = base + "/robots.txt";
//		String robotscontent = sendHTTPRequest(url);
//		ArrayList<String> robotdata = new ArrayList<String>();
//		int index1 = robotscontent.indexOf("\"");
//		int index2 = robotscontent.indexOf("\"", index1 + 1);
//		String[] robotarray;
//		if (index1 != -1 && index2 != -1) {
//			robotarray = robotscontent.substring(index1, index2).split("\n");
//		} else {
//			robotarray = robotscontent.split("\n");
//
//		}
//		for (String s : robotarray) {
//			robotdata.add(s);
//		}
//		return robotdata;
//	}


	public static boolean getAllowed(String url) {
		for (String s : ThreadSyncCrawler.disallowed) {
			if (url.contains(s.replaceAll("/*/", ""))) {
				return false;
			}
		}
		return true;
	}
}
