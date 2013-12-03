package me.dylan.Agent7;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import me.dylan.Agent7.dictionary.DictionaryLoader;
import me.dylan.Agent7.gui.FrameBruteforce;
import me.dylan.Agent7.gui.FrameDictionary;
import me.dylan.Agent7.gui.FrameFuzzer;
import me.dylan.Agent7.gui.FrameMain;
import me.dylan.Agent7.gui.ModularFrame;
import me.dylan.Agent7.gui.ProperButton;
import me.dylan.Agent7.gui.ProperMenu;
import me.dylan.Agent7.gui.ProperMenuItem;
import me.dylan.Agent7.gui.ProperMenubar;
import me.dylan.Agent7.gui.ProperTextField;
import me.dylan.Agent7.http.Fuzzer.FuzzerXSS;
import me.dylan.Agent7.proxy.ProxyScraper;
import me.dylan.Agent7.proxy.ProxySelector;
import me.dylan.Agent7.testModes.TestMode;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadBForce;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadDictionary;
import me.dylan.Agent7.testModes.TestType;

/**
 * 
 * The source and compiled code of Agent7 belong solely to Dylan T. Katz, under
 * intellectual copyright. Copyright(c) November 1st, 2013. Any libraries
 * included in this jar file belong to their respective authors.
 * 
 * Agent7(all code contained within this jar file, and it's respective sources)
 * is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * Agent7 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Agent7. If not, see <a href= "http://www.gnu.org/licenses/">licenses</a>.
 * 
 * @author Dylan T. Katz
 * @version 1.0a
 */
public class Agent7 {
	public FrameMain menu;
	BoxLayout layout;
	public static String version = "1.2a";
	public ProxySelector proxySelector;
	public static boolean fireDrillEnabled = false;
	public static ArrayList<String> warnings = new ArrayList<String>();
	/**
	 * Go as fast as possible? or hold back a bit?
	 */
	public static boolean consumeAllPossibleRes = true;
	public ProxyScraper scraper;
	public TestType testType = TestType.BRUTEFORCE;
	public FrameBruteforce bruteforceTest = new FrameBruteforce(
			"Bruteforce Tests");
	ProperMenubar menuBar;

	FrameFuzzer fuzzFrame = new FrameFuzzer("Web Tests/Fuzz Tests");
	private ProperTextField targetUrl = new ProperTextField("");
	private ProperButton submitFuzz = new ProperButton("Submit URL");

	FrameDictionary dictionaryTest;
	// De-Implemented due to the returning of garbage words.
	// ProperButton scrapeWords = new
	// ProperButton("Scrape Internet for Dictionary Words");
	public int threadCount = 0;
	public ArrayList<TestMode> running = new ArrayList<TestMode>();
	public DictionaryLoader resLoader;
	/**
	 * should we use cookies?
	 */
	public static boolean useCookies;
	public static HashMap<String, String> cookies = new HashMap<String, String>();
	public static Agent7 instance;
	private static String license =
			"*****************************************************************************************"
			+ '\n'
			+ "The source and compiled code of Agent7 belong solely to Dylan T. Katz, under "
			+ '\n'
			+ "intellectual copyright."
			+ '\n'
			+ "Any libraries/Dictionaries included in this jar belong to their respective authors. "
			+ '\n'
			+ "Copyright(c) November 1st, 2013 "
			+ '\n'
			+ '\n'
			+ "Agent7(all code contained within this jar file, and it's respective sources)"
			+ '\n'
			+ "  is free software: you can redistribute it and/or modify it under the terms of"
			+ '\n'
			+ " the GNU General Public License as published by the Free Software Foundation, "
			+ '\n'
			+ "  either version 3 of the License, or (at your option) any later version. "
			+ '\n'
			+ "  Agent7 is distributed in the hope that it will be useful,"
			+ '\n'
			+ " but WITHOUT ANY  WARRANTY; without even the implied warranty of "
			+ '\n'
			+ " MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License"
			+ '\n'
			+ " for more details.   You should have received a copy of the GNU General Public"
			+ '\n'
			+ " License along with Agent7. If not, see http://www.gnu.org/licenses/. "
			+ '\n'
			+ "*****************************************************************************************";

	public Agent7() {
		//
		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (ClassNotFoundException | InstantiationException
		// | IllegalAccessException | UnsupportedLookAndFeelException e1) {
		// e1.printStackTrace();
		// }
		initGUI();
//		proxySelector = new ProxySelector("Proxies.dat");
//		scraper = new ProxyScraper(proxySelector);
		resLoader = new DictionaryLoader();
		instance = this;
		// int count = resLoader.getAllFileContents().size();
		// if (count == 0) {
		// logLine("No dictionaries found, aborting operation.");
		// return;
		// }
		logLine("Loading...");
		if(InitialDownloadPing.getFirstTime()) {
			try {
				InitialDownloadPing.createInitFile();
				InitialDownloadPing.sendInitialDownloadPing();
			} catch(Exception e) {
				
			}
		}
		// new FuzzerXSS("http://localhost/FuzzTesting/fuzzyxss.html");
		// try {
		// scraper.scrape("Proxies.dat");
		resLoader.init();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		logLine("==========" + "Loaded Agent7 " + version + "==========");
		logLine(license);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logLine("==========" + "Awaiting commands." + "==========");
	}

	public static void main(String[] args) {
		new Agent7();

	}

	private void initGUI() {
		menu = new FrameMain();
		menu.init();
		bruteforceTest.init();
		initFTPTestBox();
		initActionListeners();
		initFuzzTestBox();

	}

	public void initFuzzTestBox() {
		fuzzFrame.init();

	}

	public void initFTPTestBox() {
		initMenuLocalPass();
		dictionaryTest = new FrameDictionary("Dictionary Tests");
		dictionaryTest.init();

	}

	public void initMenuLocalPass() {
		ArrayList<Component> components = new ArrayList<Component>();
		ProperButton threads = new ProperButton("Thread Count");
		threads.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = (String) JOptionPane.showInputDialog(menu,
						"Please enter thread count.", "Custom Thread Count",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (s != null && s.length() > 0) {
					threadCount = Integer.parseInt(s);
				}
			}
		});
		components.add(threads);
		ProperMenu testTypeChooser = new ProperMenu("Test Type");
		ProperMenuItem bruteforce = new ProperMenuItem("Brute Force");
		bruteforce.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				instance.testType = TestType.BRUTEFORCE;
			}
		});
		ProperMenuItem dictionary = new ProperMenuItem("Dictionary Attack");
		dictionary.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				instance.testType = TestType.DICTIONARY;
			}
		});
		testTypeChooser.add(bruteforce);
		testTypeChooser.add(dictionary);
		components.add(testTypeChooser);
		menuBar = new ProperMenubar(components);
	}

	

	public static void logLine(String info) {
		log("\n" + info);

	}

	public static void log(String info) {
		instance.menu.logQue.add(info);

	}

	public void initActionListeners() {
		/*
		 * scrapeWords.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { Tendril t =
		 * new Tendril(HTTPUtil.seedURL1); t.runGrowingThread(new
		 * File("websitesCrawled.dat")); } });
		 */
		menu.localTestButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bruteforceTest.setVisible(true);

			}

		});
		bruteforceTest.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for (TestMode b : running) {
					if (b instanceof TestModeLocalMultiThreadBForce)
						((TestModeLocalMultiThreadBForce) b).endTest();
					if (b instanceof TestModeLocalMultiThreadDictionary)
						((TestModeLocalMultiThreadDictionary) b).endTest();
				}
			}
		});
		menu.fuzzTestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fuzzFrame.setVisible(true);
			}
		});

		menu.ftpTestButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dictionaryTest.setVisible(true);
			}

		});
	}

	public static void err(Exception e) {
		logLine(e.getMessage());
		e.printStackTrace();
	}

	public void performCloseOperations() {
		for (TestMode tm : running) {
			tm.endTest();
		}
		System.exit(0);
	}

	/**
	 * ONLY to be used by one thread at a time.
	 * 
	 * @param progress
	 *            - the current progress as a percentage of the progress bar.
	 */
	public static void setProgress(int progress) {
		FrameMain.percentageCompleteCurrentTask = progress;
	}

}