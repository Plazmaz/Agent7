package me.dylan.Agent7.dictionary.WebCrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadSyncCrawler;
import me.dylan.Agent7.http.HTTPUtil;
import me.dylan.Agent7.http.Webpage;
import me.dylan.Agent7.http.Website;

public class Tendril {
	ArrayList<Webpage> sitesToVisit = new ArrayList<Webpage>();
	String seedURL = "";
	public volatile Thread tendrilTh;
	ArrayList<Website> myroots = new ArrayList<Website>();
	public int uniqueDomains = 0;
	public int pagesVisited = 0;
	int index=0;
	long lastUpdate = System.currentTimeMillis();
	public ArrayList<HelperThread> helpers = new ArrayList<HelperThread>();
	public Tendril(String seed) {
		seedURL = seed;
		sitesToVisit.add(new Webpage(seedURL));
	}

	public void runGrowingThread(final File file) {
		tendrilTh = new Thread(new Runnable() {
			public void run() {
				while (true) {
					lastUpdate = System.currentTimeMillis();
					try {
//						if (sitesToVisit.size() >= 30) {
//							helpers.add(new HelperThread(instance, Main.sitesVisited, sitesToVisit, file));
//							System.err.println("Spun new helper thread.");
//						}
						for (int i = 0; i < sitesToVisit.size(); i++) {
							String s = sitesToVisit.get(i).suburl;
							index=i;
							String root = "http://"+sitesToVisit.get(i).rooturl;
							if (!getIfVisited(new Webpage(root+"/robots.txt"), ThreadSyncCrawler.sitesVisited)) {
								ThreadSyncCrawler.disallowed.addAll(HTTPUtil
										.getDisallowedFromRobots(root,
												HTTPUtil.readRobots(root)));
//								sitesToVisit.add(new Webpage(root+"/sitemap.xml"));
								
							}
							if(ThreadSyncCrawler.allRoots.keySet().contains(root)){
								if(!ThreadSyncCrawler.allRoots.get(root).isTimeoutOver(System.currentTimeMillis())) {
									//Reset the queue.
									Webpage s1 = sitesToVisit.get(i);
									sitesToVisit.remove(i);
									sitesToVisit.add(s1);
									continue;
								}
							}
							sitesToVisit.remove(i);
							if (!getIfVisited(new Webpage(s), ThreadSyncCrawler.sitesVisited) && !getIfVisited(new Webpage(s), sitesToVisit)&& s != null
									&& !s.equals("http://")
									&& !s.equals("https://") && HTTPUtil.getAllowed(s)) {
								
								System.out.println(s);
								pagesVisited++;
								String rawData = HTTPUtil.sendHTTPRequest(s);
								// sitesToVisit.addAll(HTTPUtil.extractLinks(rawData,
								// "href=\"(.*?)\""));
								sitesToVisit.addAll(HTTPUtil
										.extractLinks(rawData,
												"((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"));
								ThreadSyncCrawler.sitesVisited.add(new Webpage(s));
								;
								if(s.endsWith(".html") || s.endsWith(".txt") || s.endsWith(".com") || s.endsWith(".org")) {
									
									WebCrawlerFileUtil.writeToFile(new File("tmpWordlist.dat"), rawData);
									Agent7.instance.resLoader.lookForWords(new File("tmpWordlist.dat"));
									Agent7.instance.resLoader.writeWordsToList();
								}

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
	public static boolean getIfVisited(Webpage url, ArrayList<Webpage> visited) {
		for(int i = 0; i < visited.size(); i++) {
			Webpage page = visited.get(i);
			if(page.suburl.equals(url.suburl)) {
				return true;
			}
		}
		return false;
	}
}
