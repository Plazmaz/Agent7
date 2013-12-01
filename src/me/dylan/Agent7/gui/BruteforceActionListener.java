package me.dylan.Agent7.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.testModes.TestMode;
import me.dylan.Agent7.testModes.TestModeFTPMultiThreadBForce;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadBForce;
import me.dylan.Agent7.testModes.TestType;

public class BruteforceActionListener implements ActionListener {
	FrameBruteforce parent;
	public BruteforceActionListener(FrameBruteforce parent) {
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		performLocalTest(Agent7.instance.bruteforceTest.bfPass, Agent7.instance.threadCount);

	}

	public void performLocalTest(JPasswordField password, int threadCount) {
		TestMode mode = null;
		final String passStr = new String(password.getPassword());

		Agent7.logLine("Bruteforcing Password: ");
		for (int i = 0; i < passStr.length(); i++) {
			Agent7.log("*");
		}
		if(parent.mode == TestType.LOCAL) {
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
		} else {
			int count = Agent7.instance.threadCount;
			if (Agent7.instance.threadCount <= 0)
				count = 10;
			TestModeFTPMultiThreadBForce bruteforce = new TestModeFTPMultiThreadBForce();
			String port = parent.bfPort.getText();
			if (!port.isEmpty())
				bruteforce.performTest(count,
						port, "admin");
			else {
				bruteforce.performTest(count, "21", "admin");
			}
			Agent7.instance.running.add(bruteforce);
		}
	}
}
