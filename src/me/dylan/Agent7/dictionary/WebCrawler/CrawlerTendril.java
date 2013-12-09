package me.dylan.Agent7.dictionary.WebCrawler;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.http.HTTPUtil;
import me.dylan.Agent7.http.Fuzzer.Fuzzer;
import me.dylan.Agent7.http.Fuzzer.IFuzzer;

public class CrawlerTendril {
	ArrayList<Document> sitesToVisit = new ArrayList<Document>();
	ArrayList<Document> sitesVisited = new ArrayList<Document>();
	String seedURL = "";
	public volatile Thread tendrilTh;
	public int pagesVisited = 0;
	int index = 0;
	public ArrayList<IFuzzer> tests;

	public CrawlerTendril(String seed, ArrayList<IFuzzer> tests2) {
		this.tests = tests2;
		if (!seed.startsWith("http://")) {
			seed = "http://" + seed;
		}
		seedURL = seed;
		Agent7.logLine("Connecting to site: " + seedURL);
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
								Agent7.logLine("Found external link " + root
										+ " Ignoring.");
								continue;
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
							if (!getIfVisited(doc, sitesToVisit)
									&& HTTPUtil.getAllowed(doc.baseUri())) {
								Agent7.logLine("Scanning Page: "
										+ doc.baseUri());
								pagesVisited++;
								for (Element e : doc.getElementsByAttribute("href")) {
									if(!e.attr("abs:href").isEmpty())
										sitesToVisit.add(Fuzzer.getConnection(
												e.attr("abs:href")).get());

								}
								sitesVisited.add(doc);

							}
						}

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
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
}
