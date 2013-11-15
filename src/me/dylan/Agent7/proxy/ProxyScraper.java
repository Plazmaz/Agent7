package me.dylan.Agent7.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProxyScraper {
	ProxySelector reference;
	ArrayList<String> destinations = new ArrayList<String>();
	ArrayList<String> proxies = new ArrayList<String>();

	public ProxyScraper(ProxySelector reference) {
		this.reference = reference;
		destinations
				.add("http://proxy-ip-list.com/download/free-usa-proxy-ip.txt");
		destinations
				.add("http://proxy-ip-list.com/download/free-uk-proxy-list.txt");
		destinations
				.add("http://proxy-ip-list.com/download/proxy-list-port-3128.txt");
		destinations.add("http://www.tubeincreaser.com/proxylist.txt");
		destinations.add("http://www.freeproxy.ch/proxy.txt");
	}

	public void scrape(String filename) throws MalformedURLException,
			IOException {
		proxies = reference.getProxyList();
		proxies.addAll(reference.currentProxies);
		for (String url : destinations) {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String s;
			while ((s = input.readLine()) != null) {
				s = s.replaceAll("<.*?>", "");
				System.out.println(s);
				if (s.contains(".") && s.contains(":") && !s.startsWith("#") && !Character.isAlphabetic(s.charAt(0))) {
					s = s.split("\\s+")[0];
					if (!proxies.contains(s.split(";")[0])) {
						proxies.add(s.split(";")[0]);
						System.out.println(s.split(";")[0]);
					}
				}
			}
			saveFiles(filename);
		}
	}

	public void saveFiles(String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists())
			file.createNewFile();
		PrintWriter out = new PrintWriter(file);
		for (String s : proxies) {
			out.println(s);
		}
		out.close();
	}
}
