package me.dylan.Agent7;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import me.dylan.Agent7.http.HTTPUtil;

public class ServerUtils {
	/**
	 * Send a ping to the server, telling it that it has a new download
	 */
	public static void sendInitialDownloadPing() {
		try {
			HTTPUtil.sendHTTPPing("http://192.99.10.183/Agent7-Ping.php");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Agent7.logLine("Welcome to Agent7! If you have any questions on how to use Agent7,"
				+ " please see our wiki: https://github.com/DynisDev/Agent7/wiki");

	}
	/**
	 * Update the program if needed
	 */
	public static void update() {
		String data = "";
		try {
			data = HTTPUtil.sendHTTPPing("http://192.99.10.183/version.txt");
		} catch (IOException e) {
			Agent7.logLine("Error checking for updates: "
					+ e.getLocalizedMessage());
			return;
		}
		String version = data.split("\n")[0];
		if (!Agent7.version.equalsIgnoreCase(version)) {
			String download = "https://github.com/DynisDev/Agent7/releases/download/v"
					+ version + "/Agent7_v" + version + ".jar";
			String destination = getJarPath();
			try {
				saveUrl("Agent7.tmp.jar", download);
				Agent7.logLine("Updating... File destination: " + destination);

				Runtime.getRuntime().exec(
						"java -jar Agent7.tmp.jar true " + destination);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);

			} catch (IOException e) {
				Agent7.logLine("Error: Update failed. Issue: " + e.getMessage());
				return;
			}
		}
	}

	/**
	 * Credits to
	 * http://javahowto.blogspot.com/2011/07/how-to-programmatically-copy
	 * -jar-files.html
	 */
	public static void copyJar(File src, File target) {
		if (!src.exists())
			return;
		if (!target.exists()) {
			target.mkdirs();
			try {
				target.createNewFile();
			} catch (IOException e) {
				Agent7.logLine("Failed to update: " + e.getMessage());
				return;
			}
		}
		try {
			JarFile jar = new JarFile(src);

			JarOutputStream jos = new JarOutputStream(new FileOutputStream(
					target));
			Enumeration<JarEntry> entries = jar.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				InputStream is = jar.getInputStream(entry);
				jos.putNextEntry(new JarEntry(entry.getName()));
				byte[] buffer = new byte[4096];
				int bytesRead = 0;
				while ((bytesRead = is.read(buffer)) != -1) {
					jos.write(buffer, 0, bytesRead);
				}
				is.close();
				jos.flush();
				jos.closeEntry();
			}
			jos.close();
			jar.close();
		} catch (FileNotFoundException ex) {
			Agent7.logLine("Failed to update: " + ex.getMessage());
		} catch (IOException ex) {
			Agent7.logLine("Failed to update: " + ex.getMessage());
		}
	}
	/**
	 * Save a file from a given URL
	 * @param Filename
	 * @param Url
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void saveUrl(String filename, String urlString)
			throws MalformedURLException, IOException {
		if (!new File(filename).exists()) {
			File f = new File(filename);
			f.createNewFile();
		}
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename);

			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null)
				in.close();
			if (fout != null)
				fout.close();
		}
	}
	/**
	 * Create a file so as not to send initial download ping on every boot.
	 */
	public static void createInitFile() {
		try {
			File dir = new File(System.getProperty("user.home") + "/Agent7");
			dir.mkdirs();
			File f = new File(dir, "booted.dat");
			f.createNewFile();
			PrintWriter write = new PrintWriter(f);
			write.write("This program was written by a 15 year old."
					+ "SHH! I don't want people to judge me because of it.");
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Find if we're booting for the first time
	 * @return booted
	 */
	public static boolean getFirstTime() {
		return !new File(
				(System.getProperty("user.home") + "/Agent7/booted.dat"))
				.exists();
	}
	/**
	 * Get the path to the current jar
	 * @return path
	 */
	public static String getJarPath() {
		ProtectionDomain pd = Agent7.class.getProtectionDomain();
		CodeSource cs = pd.getCodeSource();
		String destination = cs.getLocation().getPath();
		return destination;
	}
}
