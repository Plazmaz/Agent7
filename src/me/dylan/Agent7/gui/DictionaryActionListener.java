package me.dylan.Agent7.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPasswordField;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.testModes.TestMode;
import me.dylan.Agent7.testModes.TestModeFTPMultiThreadDictionary;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadDictionary;
import me.dylan.Agent7.testModes.TestType;

public class DictionaryActionListener implements ActionListener {
	FrameBruteforce parent;

	public DictionaryActionListener(FrameBruteforce parent) {
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		performTest(Agent7.instance.bruteforceTest.bfPass,
				Agent7.instance.threadCount);

	}

	public void performTest(JPasswordField password, int threadCount) {

		final String passStr = new String(password.getPassword());

		Agent7.logLine("Bruteforcing Password: ");
		for (int i = 0; i < passStr.length(); i++) {
			Agent7.log("*");
		}
		if (parent.mode == TestType.LOCAL) {
			if (!Agent7.instance.resLoader.isLoaded()) {
				Agent7.logLine("Loading dictionary files...");
				Agent7.instance.resLoader.init();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				new Thread(new Runnable() {
					@Override
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
				if (Agent7.instance.threadCount <= 0)
					Agent7.instance.threadCount = 2;
				((TestModeLocalMultiThreadDictionary) mode2).performTest(
						Agent7.instance.threadCount, passStr);
				Agent7.instance.running.add(mode2);
			}

		} else {
			Agent7.instance.resLoader.init();
			TestMode mode2 = new TestModeFTPMultiThreadDictionary();
			int count = Agent7.instance.resLoader.getCurrentWords()
					.keySet().size();
			if (count == 0) {
				Agent7.logLine("No dictionaries found, aborting operation.");
				return;
			}
			String port = parent.bfPort.getText();
			try {
				((TestModeFTPMultiThreadDictionary) mode2)
						.performTest(Agent7.instance.resLoader
								.getCurrentWords().keySet().size(),
								"admin", Integer
										.parseInt(port));
			} catch (NumberFormatException | IOException e1) {
				e1.printStackTrace();
			}
			Agent7.instance.running.add(mode2);
		}
	}
}
