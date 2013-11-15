package me.dylan.Agent7.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.dylan.Agent7.Threads.ThreadSyncCrawler;

public class HTTPUtil {
	public static final String seedURL1 = "http://www.google.com/adplanner/static/top1000/#";
	static String charset = "UTF-8";

	public static String sendHTTPRequest(String url) {
		try {
			URL destination = new URL(url);
			URLConnection connection = destination.openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("User-agent",
					"Diction - A scholarly bot");
			// Launched August 22, 2013
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charset);

			BufferedReader data = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String input = "";
			String curLine;
			while ((curLine = data.readLine()) != null) {
				input += curLine;
			}
			for (String key : connection.getHeaderFields().keySet()) {
				input += connection.getHeaderField(key);
			}

			return input.replace("<br>", "\n");
		} catch (IOException e) {

		}
		return "";
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

	public static ArrayList<String> readRobots(String base) {
		String url = base + "/robots.txt";
		String robotscontent = sendHTTPRequest(url);
		ArrayList<String> robotdata = new ArrayList<String>();
		int index1 = robotscontent.indexOf("\"");
		int index2 = robotscontent.indexOf("\"", index1 + 1);
		String[] robotarray;
		if (index1 != -1 && index2 != -1) {
			robotarray = robotscontent.substring(index1, index2).split("\n");
		} else {
			robotarray = robotscontent.split("\n");

		}
		for (String s : robotarray) {
			robotdata.add(s);
		}
		return robotdata;
	}

	public static ArrayList<Webpage> extractLinks(String rawData,
			String patternstr) {
		ArrayList<Webpage> links = new ArrayList<Webpage>();
		Pattern pattern = Pattern.compile(patternstr);
		Matcher match = pattern.matcher(rawData);
		String lastURL = seedURL1;
		while (match.find()) {
			String s = match.group(1).replace("\\", "");
			if (s.startsWith("/")) {
				s = lastURL + s;
			}
			if (s.startsWith("www")) {
				s = "http://" + s;
			}
			if (!s.equals("http://") && !s.equals("https://")
					&& !s.equals("www") && !s.equals("/")) {
				if (getAllowed(s)) {
					lastURL = s;
					links.add(new Webpage(s));
				}
			}

		}
		return links;
	}

	public static boolean getAllowed(String url) {
		for (String s : ThreadSyncCrawler.disallowed) {
			if (url.contains(s.replaceAll("/*/", ""))) {
				return false;
			}
		}
		return true;
	}
}
