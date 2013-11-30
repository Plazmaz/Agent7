package me.dylan.Agent7;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import me.dylan.Agent7.dictionary.DictionaryLoader;
import me.dylan.Agent7.gui.FTPTestActionListener;
import me.dylan.Agent7.gui.FrameMain;
import me.dylan.Agent7.gui.LocalTestActionListener;
import me.dylan.Agent7.gui.ModularFrame;
import me.dylan.Agent7.gui.ProperButton;
import me.dylan.Agent7.gui.ProperMenu;
import me.dylan.Agent7.gui.ProperMenuItem;
import me.dylan.Agent7.gui.ProperMenubar;
import me.dylan.Agent7.gui.ProperTextField;
import me.dylan.Agent7.http.Fuzzer.FuzzerIntelligent;
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
 * @version 0.0
 */
public class Agent7 {
	public FrameMain menu = new FrameMain();
	BoxLayout layout;
	public static String version = "0.1a";
	public ProxySelector proxySelector;
	public static boolean fireDrillEnabled = false;

	/**
	 * Go as fast as possible? or hold back a bit?
	 */
	public static boolean consumeAllPossibleRes = true;
	public ProxyScraper scraper;
	public TestType testType = TestType.BRUTEFORCE;
	ModularFrame localTest;
	public JPasswordField bfPass = new JPasswordField("");
	ProperButton submitBFPass = new ProperButton("Go!");
	ProperMenubar menuBar;
	/**
	 * Lol I know it's named local radio, but I assure you, it has good
	 * stations.
	 */
	JRadioButton localradio = new JRadioButton("Local(Enter Password)");
	JRadioButton ftpradio = new JRadioButton("FTP(Log in to FTP)");

	ModularFrame fuzzFrame = new ModularFrame("Web Tests/Fuzz Tests");
	private ProperTextField targetUrl = new ProperTextField("");
	private ProperButton submitFuzz = new ProperButton("Submit URL");

	ModularFrame ftpTest;
	JTextField bfPort = new JTextField();
	ProperButton submitFtpPort = new ProperButton("Submit Port");
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
	private static String license = "*********************************************************************************"
			+ '\n'
			+ "  The source and compiled code of Agent7 belong solely "
			+ +'\n'
			+ "to Dylan T. Katz, under "
			+ '\n'
			+ "  intellectual copyright."
			+ '\n'
			+ "Any libraries/Dictionaries included in this "
			+ '\n'
			+ "jar file belong to "
			+ '\n'
			+ +'\n'
			+ "  their respective authors. "
			+ '\n'
			+ "Copyright(c) November 1st, 2013 "
			+ '\n'
			+ "  Agent7(all code contained within this jar file, "
			+ '\n'
			+ "  and it's respective sources) "
			+ '\n'
			+ "  is free software: you can redistribute it and/or modify "
			+ '\n'
			+ "it under the terms of "
			+ '\n'
			+ "  the GNU General Public License as published by the Free"
			+ '\n'
			+ " Software Foundation, "
			+ '\n'
			+ "  either version 3 of the License, or (at your option) any"
			+ '\n'
			+ "later version. "
			+ '\n'
			+ "  Agent7 is distributed in the hope that it will be useful,"
			+ '\n'
			+ " but WITHOUT ANY  WARRANTY; without even the implied warranty"
			+ '\n'
			+ " of MERCHANTABILITY or FITNESS FOR "
			+ '\n'
			+ "  A PARTICULAR PURPOSE. See"
			+ '\n'
			+ " the GNU General Public License for more details. "
			+ '\n'
			+ "  You should have received a copy of the GNU"
			+ '\n'
			+ " General Public License along with "
			+ '\n'
			+ "  Agent7. If not, see http://www.gnu.org/licenses/. "
			+ '\n'
			+ "*********************************************************************************";

	public Agent7() {
		//
		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (ClassNotFoundException | InstantiationException
		// | IllegalAccessException | UnsupportedLookAndFeelException e1) {
		// e1.printStackTrace();
		// }
		initGUI();
		proxySelector = new ProxySelector("Proxies.dat");
		scraper = new ProxyScraper(proxySelector);
		resLoader = new DictionaryLoader();
		instance = this;
		// int count = resLoader.getAllFileContents().size();
		// if (count == 0) {
		// logLine("No dictionaries found, aborting operation.");
		// return;
		// }
		logLine("Loading...");
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
		menu.init();
		initBruteTestBox();
		initFTPTestBox();
		initActionListeners();
		initFuzzTestBox();

	}

	public void initBruteTestBox() {
		initMenuLocalPass();
		localTest = new ModularFrame("Testing - Brute force");
		localTest.setSize(500, 400);
		localTest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		localTest.setLayout(new GridLayout(4, 2));
		
		JPanel attackMethods = new JPanel();
		attackMethods.setBackground(Color.BLACK);
//		attackMethods.setForeground(fg);
		TitledBorder border = BorderFactory.createTitledBorder("Attack Method");
		border.setTitleColor(Color.RED);
		attackMethods.setBorder(border);
		attackMethods.setLayout(new GridLayout(2, 0));
		localradio.setBackground(Color.BLACK);
		localradio.setForeground(Color.RED);
		attackMethods.add(localradio);
		attackMethods.add(bfPass);
		ftpradio.setBackground(Color.BLACK);
		ftpradio.setForeground(Color.RED);
		attackMethods.add(ftpradio);
		attackMethods.add(bfPort);
		localTest.add(attackMethods);

	}

	public void initFuzzTestBox() {
		GridBagConstraints c = new GridBagConstraints();
		fuzzFrame.setSize(500, 200);
		fuzzFrame.setDefaultCloseOperation(ModularFrame.DISPOSE_ON_CLOSE);
		fuzzFrame.setLayout(new GridBagLayout());

		initWebFuzzerBar();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		fuzzFrame.add(menuBar, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.weightx = 400;
		c.weighty = 0;
		fuzzFrame.add(targetUrl, c);

		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 400;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		fuzzFrame.add(submitFuzz, c);

	}

	public void initFTPTestBox() {
		initMenuLocalPass();
		GridBagConstraints c = new GridBagConstraints();
		ftpTest = new ModularFrame("Testing, Please enter FTP Port");
		ftpTest.setSize(500, 200);
		ftpTest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ftpTest.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 400;
		c.weighty = 0;
		ftpTest.add(bfPort, c);

		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		ftpTest.add(menuBar, c);

		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 400;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		ftpTest.add(submitFtpPort, c);

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

	public void initWebFuzzerBar() {
		ArrayList<Component> components = new ArrayList<Component>();
		ProperButton threadcount = new ProperButton("Thread Count");
		threadcount.addActionListener(new ActionListener() {

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
		ProperButton cookies = new ProperButton("Set a cookie");
		cookies.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				String sesCookie = (String) JOptionPane.showInputDialog(menu,
						"Please enter cookie", "Cookie: ",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (sesCookie.isEmpty() || !sesCookie.contains(":")) {
					JOptionPane.showMessageDialog(fuzzFrame,
							"Invalid cookie format. Please use key:value.",
							"Invalid Cookie Format", JOptionPane.ERROR_MESSAGE);

				} else {
					Agent7.cookies.put(sesCookie.split(":")[0],
							sesCookie.split(":")[1]);
					Agent7.useCookies = true;
				}
			}
		});
		components.add(cookies);
		components.add(threadcount);
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
				localTest.setVisible(true);

			}

		});
		localTest.addWindowListener(new WindowAdapter() {
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
		submitBFPass.addActionListener(new LocalTestActionListener());
		submitFtpPort.addActionListener(new FTPTestActionListener(bfPort.getText()));
		submitFuzz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						new FuzzerXSS(instance.targetUrl.getText());

					}
				}).start();
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
				ftpTest.setVisible(true);
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
		instance.menu.percentageCompleteCurrentTask = progress;
	}

}