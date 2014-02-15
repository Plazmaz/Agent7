package me.dylan.Agent7;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import me.dylan.Agent7.dictionary.DictionaryLoader;
import me.dylan.Agent7.gui.FrameBruteforce;
import me.dylan.Agent7.gui.FrameDictionary;
import me.dylan.Agent7.gui.FrameFuzzer;
import me.dylan.Agent7.gui.FrameMain;
import me.dylan.Agent7.gui.FrameResult;
import me.dylan.Agent7.gui.ProperButton;
import me.dylan.Agent7.gui.ProperMenu;
import me.dylan.Agent7.gui.ProperMenuItem;
import me.dylan.Agent7.gui.ProperMenubar;
import me.dylan.Agent7.proxy.ProxyScraper;
import me.dylan.Agent7.proxy.ProxySelector;
import me.dylan.Agent7.testModes.TestMode;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadBForce;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadDictionary;
import me.dylan.Agent7.testModes.TestType;

/**
 * 
 * For License, see http://creativecommons.org/licenses/by-sa/4.0/
 * @author Dylan T. Katz
 * @version 1.3.1a
 */
public class Agent7 {
	public FrameMain menu;
	public static FrameResult results = new FrameResult();
	BoxLayout layout;
	public static String version = "1.3.1a";
	public ProxySelector proxySelector;
	public static boolean fireDrillEnabled = false;
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

	FrameDictionary dictionaryTest;
	public int threadCount = 0;
	public ArrayList<TestMode> running = new ArrayList<TestMode>();
	public DictionaryLoader resLoader;
	/**
	 * should we use cookies?
	 */
	public static boolean useCookies;
	public static HashMap<String, String> cookies = new HashMap<String, String>();
	public static Agent7 instance;
	private static String license = "*****************************************************************************************"
			+ '\n'
			+ "For The license agreement of this program, please see "
			+ '\n'
			+ "http://creativecommons.org/licenses/by-sa/4.0/"
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
		// proxySelector = new ProxySelector("Proxies.dat");
		// scraper = new ProxyScraper(proxySelector);
		resLoader = new DictionaryLoader();
		instance = this;
		// int count = resLoader.getAllFileContents().size();
		// if (count == 0) {
		// logLine("No dictionaries found, aborting operation.");
		// return;
		// }
		logLine("Loading...");
		/*if (ServerUtils.getFirstTime()) {
			try {
				ServerUtils.createInitFile();
				ServerUtils.sendInitialDownloadPing();
			} catch (Exception e) {

			}
		}
		ServerUtils.update();*/
		resLoader.init();

		logLine("==========" + "Loaded Agent7 " + version + "==========");
		logLine(license);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logLine("==========" + "Awaiting commands." + "==========");

		initActionListeners();
	}

	public static void main(String[] args) {
		if (args.length > 1) {
			if (Boolean.getBoolean(args[0])) {
				File file = new File("Agent7.tmp.jar");
				ServerUtils.copyJar(file, new File(args[1]));
				file.delete();

			}
		}
		new Agent7();

	}

	private void initGUI() {
		menu = new FrameMain();
		menu.init();
		bruteforceTest.init();
		initDictionaryTestBox();
		initFuzzTestBox();

	}

	public void initFuzzTestBox() {
		fuzzFrame.init();

	}

	public void initDictionaryTestBox() {
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
