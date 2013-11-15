package me.dylan.Agent7.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import me.dylan.Agent7.Threads.ThreadBruteforceFTP;

public class ProxySelector {
	File proxyfile;
	private ArrayList<String> availableProxies = new ArrayList<String>();
	ArrayList<String> currentProxies = new ArrayList<String>();
	private Random random = new Random();
	public ProxySelector() {
		proxyfile = new File(this.getClass().getResource("Proxies.dat").getPath().replace("%20", " "));
		if (!proxyfile.exists()) {
			throw new IllegalArgumentException(
					"Failed to find internal proxy file.");
		}
		try {
			getAllProxies();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public String getRandomProxy() {
		if(availableProxies.size()<=0)
			return ThreadBruteforceFTP.ip+":21";
		String proxy = availableProxies.get(random.nextInt(availableProxies.size()));
		availableProxies.remove(proxy);
		currentProxies.add(proxy);
		return proxy;
	}
	public void finishedWithProxy(String proxy) {
		currentProxies.remove(proxy);
		availableProxies.add(proxy);
	}
	public ProxySelector(String filename) {
		proxyfile = new File(filename);
		if (!proxyfile.exists())
			throw new IllegalArgumentException("Invalid proxy file name.");
		try {
			getAllProxies();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getAllProxies() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(proxyfile));
		String line;
		while ((line = in.readLine()) != null) {
			if (line.contains(":") && line.contains(".")) {
				availableProxies.add(line);
			}
		}
		in.close();
	}
	public ArrayList<String> getProxyList() {
		return availableProxies;
	}
	public void remove(String proxy) {
		availableProxies.remove(proxy);
//		try {
//			syncToFile();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	}
	public void syncToFile() throws FileNotFoundException {
		PrintWriter out = new PrintWriter(proxyfile);
		for(String s : availableProxies) {
			out.println(s);
		}
		out.close();
	}
}
