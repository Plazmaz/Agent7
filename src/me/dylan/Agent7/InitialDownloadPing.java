package me.dylan.Agent7;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import me.dylan.Agent7.dictionary.DictionaryFileUtil;
import me.dylan.Agent7.dictionary.WebCrawler.WebCrawlerFileUtil;
import me.dylan.Agent7.http.HTTPUtil;

/**
 * This class is simply to gather the amount of downloads. I don't creep on any
 * of you guys. The source for the server is here:
 * https://github.com/DynisDev/Agent7-Web
 * 
 * @author Dylan
 * 
 */
public class InitialDownloadPing {
	public static void sendInitialDownloadPing() {
		try {
			Agent7.logLine(HTTPUtil.sendHTTPPing("http://a7pi.zxq.net/Agent7-Ping.php"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Agent7.logLine("Welcome to Agent7! If you have any questions on how to use Agent7,"
				+ " please see our wiki: https://github.com/DynisDev/Agent7/wiki");

	}

	public static void createInitFile() {
		try {
			File dir = new File(System.getProperty("user.home") + "/Agent7");
			dir.mkdirs();
			new File(dir, "booted.dat").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean getFirstTime() {
		return !new File(
				(System.getProperty("user.home") + "/Agent7/booted.dat"))
				.exists();
	}
}
