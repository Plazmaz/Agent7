package me.dylan.Agent7.http;

import java.awt.image.BufferedImage;

public class Website {
	public Website parent;
	public String rooturl;
	public long timeLastVisited = System.currentTimeMillis();
	public long timeout = 3000;
	BufferedImage icon;
	public Website(String url, BufferedImage icon) {
		this.icon = icon;
		rooturl = url;
	}
	public boolean isTimeoutOver(long time) {
		if(Math.abs(time-timeLastVisited)>=timeout) {
			timeLastVisited = System.currentTimeMillis();
			return true;
		}
		return false;
	}
}
