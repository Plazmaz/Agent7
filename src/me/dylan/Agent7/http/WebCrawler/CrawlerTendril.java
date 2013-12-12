package me.dylan.Agent7.http.WebCrawler;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadSyncCrawler;
import me.dylan.Agent7.gui.FrameFuzzer;
import me.dylan.Agent7.http.HTTPUtil;
import me.dylan.Agent7.http.Fuzzer.Fuzzer;
import me.dylan.Agent7.http.Fuzzer.IFuzzer;

public class CrawlerTendril {
	ArrayList<Document> sitesToVisit = new ArrayList<Document>();
	String seedURL = "";
	public volatile Thread tendrilTh;
	public int pagesVisited = 0;
	int index = 0;
	public ArrayList<IFuzzer> tests;
	ArrayList<String> urlDepth = new ArrayList<String>();

	public CrawlerTendril(String seed, ArrayList<IFuzzer> tests2) {
		this.tests = tests2;
		if (!seed.startsWith("http://")) {
			seed = "http://" + seed;
		}
		seedURL = seed;
		log("Connecting to site: " + seedURL);
		urlDepth.add(seedURL);
		try {
			sitesToVisit.add(Fuzzer.getConnection(seedURL).get());
		} catch (IOException e) {
			Agent7.err(e);
		}
	}

	public void runGrowingThread() {
		tendrilTh = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						for (int i = 0; i < sitesToVisit.size(); i++) {
							index = i;
							String root = sitesToVisit.get(i).baseUri()
									.split("/")[2];
							Document doc = sitesToVisit.get(i);
							if (!root.equals(seedURL.split("/")[2])) {
								if (!urlDepth.contains(root)) {
									if (urlDepth.size() < (int) FrameFuzzer.depth
											.getValue()) {
										urlDepth.add(root);
										log("Found external link " + root
												+ " Due to depth settings, we're visiting this page.");
									} else {
										log("Found external link " + root
												+ " Ignoring.");
										sitesToVisit.remove(i);
										continue;
									}
								}
							}
							for (IFuzzer f : tests) {
								f.setUrl(doc.baseUri());
								f.initializeAttack();
							}
							/*
							 * try { if (!getIfVisited( Fuzzer.getConnection(
							 * "http://" + root + "/robots.txt").get(),
							 * sitesVisited)) {
							 * ThreadSyncCrawler.disallowed.addAll(HTTPUtil
							 * .getDisallowedFromRobots(root,
							 * HTTPUtil.readRobots(root)));
							 * 
							 * } } catch (Exception e) { }
							 */
							sitesToVisit.remove(i);
							if (!getIfVisited(doc,
									ThreadSyncCrawler.sitesVisited)
									&& !getIfVisited(doc, sitesToVisit)
									&& HTTPUtil.getAllowed(doc.baseUri())) {

								log("Scanning Page: " + doc.baseUri());
								pagesVisited++;
								for (Element e : doc
										.getElementsByAttribute("href")) {
									if (!e.attr("abs:href").isEmpty())
										sitesToVisit.add(Fuzzer.getConnection(
												e.attr("abs:href")).get());

								}
								ThreadSyncCrawler.sitesVisited.add(doc);

							}
						}
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if(sitesToVisit.size() <= 0)
						break;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				log("Crawling Complete, starting point: "+seedURL);
			}
		});
		tendrilTh.start();
	}

	public static boolean getIfVisited(Document doc,
			ArrayList<Document> sitesVisited) {
		for (int i = 0; i < sitesVisited.size(); i++) {
			Document page = sitesVisited.get(i);
			if (page.baseUri().equals(doc.baseUri())) {
				return true;
			}
		}
		return false;
	}

	public static void log(String info) {
		Agent7.logLine("[Crawler] " + info);
	}
}
