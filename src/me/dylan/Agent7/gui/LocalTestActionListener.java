package me.dylan.Agent7.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.testModes.TestMode;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadBForce;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadDictionary;
import me.dylan.Agent7.testModes.TestType;

public class LocalTestActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		performLocalTest(Agent7.instance.password, Agent7.instance.threadCount);

	}

	public void performLocalTest(JPasswordField password, int threadCount) {
		TestMode mode = null;
		final String passStr = new String(password.getPassword());

		Agent7.logLine("Testing Local Password: ");
		for (int i = 0; i < passStr.length(); i++) {
			Agent7.log("*");
		}
		if (Agent7.instance.testType == TestType.BRUTEFORCE) {
			mode = new TestModeLocalMultiThreadBForce();
			TestModeLocalMultiThreadBForce passTask = (TestModeLocalMultiThreadBForce) mode;

			if (threadCount > 0 && passStr.length() % threadCount == 0)
				passTask.performTest(passStr, threadCount);
			else {
				// if (passStr.length() % 2 == 0)
				// passTask.performTest(passStr, 2);
				// else
				passTask.performTest(passStr, 1);
				mode = passTask;
				Agent7.instance.running.add(mode);

			}
		} else if (Agent7.instance.testType == TestType.DICTIONARY) {
			if (!Agent7.instance.resLoader.isLoaded()) {
				Agent7.logLine("Loading dictionary files...");
				Agent7.instance.resLoader.init();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				new Thread(new Runnable() {
					public void run() {
						TestMode mode2 = new TestModeLocalMultiThreadDictionary();
						((TestModeLocalMultiThreadDictionary) mode2)
								.performTest(Agent7.instance.resLoader
										.getCurrentWords().keySet().size(),
										passStr);
						Agent7.instance.running.add(mode2);
					}
				}).start();

			} else {
				TestMode mode2 = new TestModeLocalMultiThreadDictionary();
				int count = Agent7.instance.resLoader.getCurrentWords()
						.keySet().size();
				if (count == 0) {
					Agent7.logLine("No dictionaries found, aborting operation.");
					return;
				}
				if(Agent7.instance.threadCount <= 0)
					Agent7.instance.threadCount = 2;
				((TestModeLocalMultiThreadDictionary) mode2).performTest(
						Agent7.instance.threadCount, passStr);
				Agent7.instance.running.add(mode2);
			}

		}

	}
}
