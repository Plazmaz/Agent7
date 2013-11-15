package me.dylan.Agent7.Threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import me.dylan.Agent7.dictionary.WebCrawler.Tendril;
import me.dylan.Agent7.http.Webpage;
import me.dylan.Agent7.http.Website;


public class ThreadSyncCrawler {
	public static ArrayList<Tendril> tendrils = new ArrayList<Tendril>();
	public static ArrayList<Webpage> sitesVisited = new ArrayList<Webpage>();
	public static HashMap<String, Website> allRoots = new HashMap<String, Website>();
	public static AtomicInteger uid = new AtomicInteger(0);
	public static ArrayList<String> disallowed = new ArrayList<String>();
}
