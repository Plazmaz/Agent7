package me.dylan.Agent7.http.Fuzzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URISyntaxException;
import java.util.ArrayList;

import me.dylan.Agent7.res.ContentLoader;

public class PayloadUtil {
	public static ArrayList<String> getInjectionPayloads(String fileStr)
			throws IOException {
		ArrayList<String> payloads = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(ContentLoader.getInternalFile(fileStr)));

		String s = "";
		while ((s = in.readLine()) != null) {
			if (!s.startsWith("##")) {
				payloads.add(s);
			}
		}

		in.close();
		return payloads;
	}
}
