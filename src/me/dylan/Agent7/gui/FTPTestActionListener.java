package me.dylan.Agent7.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.testModes.TestMode;
import me.dylan.Agent7.testModes.TestModeFTPMultiThreadBForce;
import me.dylan.Agent7.testModes.TestModeFTPMultiThreadDictionary;
import me.dylan.Agent7.testModes.TestType;

public class FTPTestActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		performFTPTest();
	}

	public void performFTPTest() {
		if (Agent7.instance.testType == TestType.BRUTEFORCE) {
			int count = Agent7.instance.threadCount;
			if (Agent7.instance.threadCount <= 0)
				count = 10;
			TestModeFTPMultiThreadBForce bruteforce = new TestModeFTPMultiThreadBForce();

			if (!Agent7.instance.ftpPort.getText().isEmpty())
				bruteforce.performTest(count,
						Agent7.instance.ftpPort.getText(), "admin");
			else {
				bruteforce.performTest(count, "21", "admin");
			}
			Agent7.instance.running.add(bruteforce);
		} else if (Agent7.instance.testType == TestType.DICTIONARY) {
				if (!Agent7.instance.resLoader.isLoaded()) {
					new Thread(new Runnable() {
						public void run() {
							TestMode mode2 = new TestModeFTPMultiThreadDictionary();
							try {
								if (!Agent7.instance.ftpPort.getText().isEmpty()) {
								((TestModeFTPMultiThreadDictionary) mode2).performTest(
										Agent7.instance.resLoader
												.getCurrentWords().keySet()
												.size(),
										"admin",
										Integer.parseInt(Agent7.instance.ftpPort
												.getText()));
								} else {
									((TestModeFTPMultiThreadDictionary) mode2).performTest(
											Agent7.instance.resLoader
													.getCurrentWords().keySet()
													.size(),
											"admin",
											21);
								}

							} catch (NumberFormatException | IOException e) {
								e.printStackTrace();
							}
							Agent7.instance.running.add(mode2);
						}
					}).start();
				
			} else {
				Agent7.instance.resLoader.init();
				TestMode mode2 = new TestModeFTPMultiThreadDictionary();
				int count = Agent7.instance.resLoader.getCurrentWords()
						.keySet().size();
				if (count == 0) {
					Agent7.logLine("No dictionaries found, aborting operation.");
					return;
				}
				try {
					((TestModeFTPMultiThreadDictionary) mode2)
							.performTest(Agent7.instance.resLoader
									.getCurrentWords().keySet().size(),
									"admin", Integer
											.parseInt(Agent7.instance.ftpPort
													.getText()));
				} catch (NumberFormatException | IOException e1) {
					e1.printStackTrace();
				}
				Agent7.instance.running.add(mode2);
			}

		}
	}
}
