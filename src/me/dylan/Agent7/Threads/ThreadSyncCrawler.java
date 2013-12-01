package me.dylan.Agent7.Threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;

import me.dylan.Agent7.dictionary.WebCrawler.CrawlerTendril;


public class ThreadSyncCrawler {
	public static ArrayList<CrawlerTendril> tendrils = new ArrayList<CrawlerTendril>();
	public static ArrayList<Document> sitesVisited = new ArrayList<Document>();
	public static HashMap<String, Document> allRoots = new HashMap<String, Document>();
	public static AtomicInteger uid = new AtomicInteger(0);
	public static ArrayList<String> disallowed = new ArrayList<String>();
}
