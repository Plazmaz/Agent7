package me.dylan.Agent7.http.WebCrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import me.dylan.Agent7.http.HTTPUtil;
import me.dylan.Agent7.http.Webpage;

public class WebCrawlerFileUtil {
	public static File hookIntoFile(String path) throws IOException {
		File f;
		if (!(f = new File(path)).exists()) {
			f.createNewFile();
		}
		return f;
	}

	public static void writeToFile(File target, String contents)
			throws IOException {
		PrintWriter print = new PrintWriter(new FileOutputStream(target, true));
		String[] data = contents.split("\n");
		for (String s : data) {
			print.println(s);
		}
		print.close();
	}

	public static ArrayList<Webpage> getAllURLS(File file) throws IOException {
		String s = "";
		ArrayList<Webpage> tmp = new ArrayList<Webpage>();
		BufferedReader data = new BufferedReader(new FileReader(file));
		while ((s = data.readLine()) != null) {
			if (HTTPUtil.getAllowed(s) && !s.contains("favicon.")
					&& !s.endsWith("robots.txt")
					&& !s.equals("==========DOWNLOADED IMAGE==========")
					&& !s.equals("Initialize Data Block.")) {
				tmp.add(new Webpage(s));
			}
		}
		data.close();
		return tmp;
	}
}
