package me.dylan.Agent7;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import me.dylan.Agent7.dictionary.DictionaryLoader;
import me.dylan.Agent7.gui.FTPTestActionListener;
import me.dylan.Agent7.gui.FrameMain;
import me.dylan.Agent7.gui.LocalTestActionListener;
import me.dylan.Agent7.gui.ProperButton;
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
 * intellectual copyright. Any libraries included in this jar file belong to
 * their respective authors. Copyright(c) November 1st, 2013
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
	FrameMain menu;
	BoxLayout layout;
	public static String version = "0.0";
	JTextArea output;
	JScrollPane scroll;
	public ProxySelector proxySelector;
	public static boolean fireDrillEnabled = false;

	/**
	 * Go as fast as possible? or hold back a bit?
	 */
	public static boolean consumeAllPossibleRes = true;
	private ArrayList<String> logQue = new ArrayList<String>();
	public ProxyScraper scraper;
	public TestType testType = TestType.BRUTEFORCE;
	JFrame localTest;
	ProperButton localTestButton;
	public JPasswordField password = new JPasswordField("");
	ProperButton submitLocalPass = new ProperButton("Enter");
	JMenuBar menuBar;

	JFrame xssFuzzFrame;
	ProperButton xssTestButton;
	private ProperTextField targetUrl = new ProperTextField("");
	private ProperButton fuzzXSS;

	JFrame ftpTest;
	ProperButton ftpTestButton;
	public ProperTextField ftpPort = new ProperTextField("");
	ProperButton submitFtpPort = new ProperButton("Submit Port");
	JProgressBar progressInCurrentTask = new JProgressBar();
	// De-Implemented due to the returning of garbage words.
	// JButton scrapeWords = new
	// JButton("Scrape Internet for Dictionary Words");
	public int threadCount = 0;
	public ArrayList<TestMode> running = new ArrayList<TestMode>();
	public DictionaryLoader resLoader;
	public static Agent7 instance;
	private static String license = "*********************************************************************************"
			+ '\n'
			+ "  The source and compiled code of Agent7 belong solely to Dylan T. Katz, under "
			+ '\n'
			+ "  intellectual copyright. Any libraries included in this jar file belong to "
			+ '\n'
			+ "  their respective authors. Copyright(c) November 1st, 2013 "
			+ '\n'
			+ "  Agent7(all code contained within this jar file, and it's respective sources) "
			+ '\n'
			+ "  is free software: you can redistribute it and/or modify it under the terms of "
			+ '\n'
			+ "  the GNU General Public License as published by the Free Software Foundation, "
			+ '\n'
			+ "  either version 3 of the License, or (at your option) any later version. "
			+ '\n'
			+ "  Agent7 is distributed in the hope that it will be useful, but WITHOUT ANY "
			+ '\n'
			+ "  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR "
			+ '\n'
			+ "  A PARTICULAR PURPOSE. See the GNU General Public License for more details. "
			+ '\n'
			+ "  You should have received a copy of the GNU General Public License along with "
			+ '\n'
			+ "  Agent7. If not, see http://www.gnu.org/licenses/. "
			+ '\n'
			+ "*********************************************************************************";
	public static int percentageCompleteCurrentTask = 0;

	public Agent7() {
		initButtons();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
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
		logLine(license);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logLine("Loading...");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// new FuzzerXSS("http://localhost/FuzzTesting/fuzzyxss.html");
		try {
			scraper.scrape("Proxies.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}

		logLine("==========" + "Loaded Agent7 " + version + "==========");
		logLine("==========" + "Awaiting commands." + "==========");
	}

	public static void main(String[] args) {
		new Agent7();

	}

	private void initButtons() {
		localTestButton = new ProperButton("Test Local Password");
		ftpTestButton = new ProperButton("Test FTP Password - ONLY YOUR FTP!");
		xssTestButton = new ProperButton("Begin XSS Fuzzing");
		fuzzXSS = new ProperButton("Submit URL");

	}

	private void initGUI() {
		menu = new FrameMain();
		menu.setSize(700, 630);
		xssFuzzFrame = new JFrame("XSS Fuzzing");
		progressInCurrentTask.setForeground(Color.RED);
		progressInCurrentTask.setBackground(Color.BLACK);
		progressInCurrentTask.setBorderPainted(false);
		progressInCurrentTask.setStringPainted(true);
		output = new JTextArea(10, 80);
		output.setBackground(Color.BLACK);
		output.setForeground(Color.GREEN);
		// TODO: Replace with a custom class(maybe.)
		output.setEditable(false);
		scroll = new JScrollPane(output);
		scroll.setFont(new Font("Helvetica", Font.ITALIC, 12));
		scroll.setAutoscrolls(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		// constraints.gridwidth = 80;
		// constraints.gridheight = 50;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.insets = new Insets(100, 0, 0, 0);
		menu.add(localTestButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(-50, 0, 0, 0);
		menu.add(ftpTestButton, constraints);

		constraints.insets = new Insets(-70, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 2;
		menu.add(xssTestButton, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.insets = new Insets(-50, 0, 0, 0);
		menu.add(progressInCurrentTask, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 4;
		menu.add(scroll, constraints);
		// menu.add(scrapeWords, menu);
		menu.setVisible(true);
		beginGUIUpdates();
		initLocalTestBox();
		initFTPTestBox();
		initActionListeners();
		initXSSTestBox();

	}

	public void initLocalTestBox() {
		initMenuLocalPass();
		GridBagConstraints c = new GridBagConstraints();
		localTest = new JFrame("Testing Local Password");
		localTest.setSize(500, 200);
		localTest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		localTest.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 400;
		c.weighty = 0;
		localTest.add(password, c);

		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.NORTHWEST;
		localTest.add(menuBar, c);

		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 400;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		localTest.add(submitLocalPass, c);

	}
	
	public void initXSSTestBox() {
		GridBagConstraints c = new GridBagConstraints();
		xssFuzzFrame.setSize(500, 200);
		xssFuzzFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		xssFuzzFrame.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 400;
		c.weighty = 0;
		xssFuzzFrame.add(targetUrl, c);


		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 400;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		xssFuzzFrame.add(fuzzXSS, c);

	}
	public void initFTPTestBox() {
		initMenuLocalPass();
		GridBagConstraints c = new GridBagConstraints();
		ftpTest = new JFrame("Testing, Please enter FTP Port");
		ftpTest.setSize(500, 200);
		ftpTest.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ftpTest.setLayout(new GridBagLayout());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 400;
		c.weighty = 0;
		ftpTest.add(ftpPort, c);

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
		menuBar = new JMenuBar();
		JMenu threads = new JMenu("Threads");
		JMenuItem single = new JMenuItem("Single Thread");
		single.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				instance.threadCount = 1;
			}
		});
		JMenuItem dual = new JMenuItem("Dual Threads");
		dual.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				instance.threadCount = 2;
			}
		});
		JMenuItem tri = new JMenuItem("Triple Threads");
		tri.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				instance.threadCount = 3;
			}
		});
		JMenuItem nonspecific = new JMenuItem("Custom amount");
		nonspecific.addActionListener(new ActionListener() {

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
		threads.add(single);
		threads.add(dual);
		threads.add(tri);
		threads.add(nonspecific);
		menuBar.add(threads);
		JMenu testTypeChooser = new JMenu("Test Type");
		JMenuItem bruteforce = new JMenuItem("Brute Force");
		bruteforce.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				instance.testType = TestType.BRUTEFORCE;
			}
		});
		JMenuItem dictionary = new JMenuItem("Dictionary Attack");
		dictionary.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				instance.testType = TestType.DICTIONARY;
			}
		});
		testTypeChooser.add(bruteforce);
		testTypeChooser.add(dictionary);
		menuBar.add(testTypeChooser);
	}

	public static void logLine(String info) {
		log("\n" + info);

	}

	public static void log(String info) {
		instance.logQue.add(info);

	}

	private void beginGUIUpdates() {
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				ArrayList<String> logData = new ArrayList<String>();
				while (true) {
					progressInCurrentTask
							.setValue(percentageCompleteCurrentTask);
					if (percentageCompleteCurrentTask > 0
							&& percentageCompleteCurrentTask < 100) {

						if (menu.getContentPane().getCursor().getType() != Cursor.WAIT_CURSOR) {
							menu.getContentPane().setCursor(
									new Cursor(Cursor.WAIT_CURSOR));
						}
					} else {
						if (menu.getContentPane().getCursor().getType() != Cursor.DEFAULT_CURSOR) {
							menu.getContentPane().setCursor(
									new Cursor(Cursor.DEFAULT_CURSOR));
						}

					}

					for (String info : (ArrayList<String>) logQue.clone()) {
						if (info == null)
							continue;
						if (output.getLineCount() >= 2000) {
							output.setText("");
						}
						logData.add(info);
						// System.out.println(info);
						output.setText(output.getText() + info);
						JScrollBar sbar = scroll.getVerticalScrollBar();
						if (output.getLineCount() >= 1) {
							output.scrollRectToVisible(output.getVisibleRect());
							sbar.setValue(sbar.getMaximum());
							sbar.repaint();
						}
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					logQue.removeAll(logData);
				}
			}
		}).start();
	}

	public void initActionListeners() {
		/*
		 * scrapeWords.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) { Tendril t =
		 * new Tendril(HTTPUtil.seedURL1); t.runGrowingThread(new
		 * File("websitesCrawled.dat")); } });
		 */
		localTestButton.addActionListener(new ActionListener() {

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
		submitLocalPass.addActionListener(new LocalTestActionListener());
		submitFtpPort.addActionListener(new FTPTestActionListener());
		fuzzXSS.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					public void run() {
						new FuzzerXSS(instance.targetUrl.getText());
						
					}
				}).start();
			}
		});
		xssTestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				xssFuzzFrame.setVisible(true);
			}
		});
		
		ftpTestButton.addActionListener(new ActionListener() {

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
		percentageCompleteCurrentTask = progress;
	}

}