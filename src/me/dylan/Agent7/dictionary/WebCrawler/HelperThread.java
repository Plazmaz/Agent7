package me.dylan.Agent7.dictionary.WebCrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadSyncCrawler;
import me.dylan.Agent7.http.HTTPUtil;
import me.dylan.Agent7.http.Webpage;

public class HelperThread {
	Thread thread;
	Tendril parent;
	public HelperThread(final Tendril parent, final ArrayList<Webpage> visited, final ArrayList<Webpage> unvisited, final File file) {
		this.parent = parent;
		thread = new Thread(new Runnable() {
			public void run() {
				for(int i=unvisited.size()-1; i > parent.index+1;i--) {
						try {
							updateHelper(file, unvisited, visited, i);
						} catch (IOException e) {
//							e.printStackTrace();
							System.out.println("No robots found.");
						}
					}


					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		});
		thread.start();
	}

	private void updateHelper(final File file, ArrayList<Webpage> sitesToVisit,
			ArrayList<Webpage> sitesVisited, int index) throws IOException {
			Webpage w = sitesToVisit.get(index);
			sitesToVisit.remove(index);
			String root = "http://"+w.rooturl;
			 if(!ThreadSyncCrawler.allRoots.keySet().contains(root)) {
				if(!ThreadSyncCrawler.allRoots.get(root).isTimeoutOver(System.currentTimeMillis())) {
					//Reset the queue.
					Webpage s1 = sitesToVisit.get(index);
					sitesToVisit.remove(index);
					sitesToVisit.add(s1);
					return;
				}
			}
			if (!sitesVisited.contains(root + "/robots.txt")) {
				ThreadSyncCrawler.disallowed.addAll(HTTPUtil
						.getDisallowedFromRobots(root,
								HTTPUtil.readRobots(root)));
				sitesToVisit.add(new Webpage(root+"/robots.txt"));
				
			}
			if (!sitesVisited.contains(w) && w != null && !w.equals("http://")
					&& !w.equals("https://")) {
				System.out.println(w.suburl);
				String rawData = HTTPUtil.sendHTTPRequest(w.suburl);
				// sitesToVisit.addAll(HTTPUtil.extractLinks(rawData,
				// "href=\"(.*?)\""));
				sitesToVisit
						.addAll(HTTPUtil
								.extractLinks(rawData,
										"((https?):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"));
				sitesVisited.add(w);
				if(w.suburl.endsWith(".html") || w.suburl.endsWith(".txt")) {
					
					WebCrawlerFileUtil.writeToFile(new File("tmpWordlist.dat"), rawData);
					Agent7.instance.resLoader.lookForWords(file);
				}
				

		}
	}
}
