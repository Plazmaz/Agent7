package me.dylan.Agent7.http;

import me.dylan.Agent7.Threads.ThreadSyncCrawler;

public class Webpage extends Website {
	public String suburl;
	public int uid = ThreadSyncCrawler.uid.getAndIncrement();
	public Webpage(String url) {
		super(url.split("/")[2], null);
		suburl = url;
	}

}
