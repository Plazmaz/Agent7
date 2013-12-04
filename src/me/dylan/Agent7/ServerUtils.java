package me.dylan.Agent7;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import me.dylan.Agent7.http.HTTPUtil;

/**
 * This class is simply to gather the amount of downloads, and auto-update. I
 * don't creep on any of you guys, don't worry.
 * 
 * @author Dylan
 * 
 */
public class ServerUtils {
	public static void sendInitialDownloadPing() {
		try {
			HTTPUtil.sendHTTPPing("http://a7pi.zxq.net/Agent7-Ping.php");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Agent7.logLine("Welcome to Agent7! If you have any questions on how to use Agent7,"
				+ " please see our wiki: https://github.com/DynisDev/Agent7/wiki");

	}

	public static void update() {
		String data = "";
		try {
			data = HTTPUtil.sendHTTPPing("http://a7pi.zxq.net/version.txt");
		} catch (IOException e) {
			Agent7.logLine("Error updating program: " + e.getMessage());
		}
		String version = data.split("\n")[0];
		if (!Agent7.version.equalsIgnoreCase(version)) {
			version = version.replaceAll("a", "").replaceAll("b", "");
			String download = "https://drone.io/github.com/DynisDev/Agent7/files/target/Agent7-"
					+ version + ".jar";
			String destination = getJarPath();
			try {
				saveUrl("Agent7.tmp.jar", download);
				Agent7.logLine("Updating... File destination: " + destination);

				Runtime.getRuntime().exec("java -jar Agent7.tmp.jar true "+ destination);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
		}
	}
	/**
	 * Credits to http://javahowto.blogspot.com/2011/07/how-to-programmatically-copy-jar-files.html
	 */
	public static void copyJar(File src, File target) {
		if(!src.exists())
			return;
		if(!target.exists()) {
			target.mkdirs();
			try {
				target.createNewFile();
			} catch (IOException e) {
				Agent7.logLine("Failed to update: "+e.getMessage());
				return;
			}
		}
		try {
	       JarFile jar = new JarFile(src);
	 
	       JarOutputStream jos = new JarOutputStream(new FileOutputStream(target));
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
			Agent7.logLine("Failed to update: "+ex.getMessage());
		} catch (IOException ex) {
			Agent7.logLine("Failed to update: "+ex.getMessage());
		}
	}

	public static void saveUrl(String filename, String urlString)
			throws MalformedURLException, IOException {
		if(!new File(filename).exists()) {
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

	public static void createInitFile() {
		try {
			File dir = new File(System.getProperty("user.home") + "/Agent7");
			dir.mkdirs();
			new File(dir, "booted.dat").createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean getFirstTime() {
		return !new File(
				(System.getProperty("user.home") + "/Agent7/booted.dat"))
				.exists();
	}
	
	public static String getJarPath() {
		ProtectionDomain pd= Agent7.class.getProtectionDomain();
		CodeSource cs= pd.getCodeSource();
		String destination = cs.getLocation().getPath();
		return destination;
	}
}
