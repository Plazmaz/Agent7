package me.dylan.Agent7.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import me.dylan.Agent7.http.Fuzzer.Fuzzer;
import me.dylan.Agent7.http.Fuzzer.FuzzerPhp;
import me.dylan.Agent7.http.Fuzzer.FuzzerSQL;
import me.dylan.Agent7.http.Fuzzer.FuzzerSQLBlind;
import me.dylan.Agent7.http.Fuzzer.FuzzerXSS;
import me.dylan.Agent7.http.Fuzzer.IFuzzer;

public class FuzzSubmitActionListener implements ActionListener {
	FrameFuzzer parent;

	public FuzzSubmitActionListener(FrameFuzzer fuzzer) {
		this.parent = fuzzer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(new Runnable() {
			public void run() {
				for (JCheckBox box : parent.attackOptions) {
					if (box.isSelected()) {
						final IFuzzer fuzzer = initializeTestForBox(box,
								parent.url.getText());
						fuzzer.initializeAttack();
					}
				}
			}
		}).start();
	}

	private IFuzzer initializeTestForBox(JCheckBox box, String url) {
		switch (box.getText()) {
		case "SQL Injection(Errors)":
			return new FuzzerSQL(url);
		case "PHP Injection(NOT CURRENTLY FULLY FUNCTIONAL)":
			return new FuzzerPhp(url);
		case "XSS(Script Injection)":
			return new FuzzerXSS(url);
		case "SQL Injection(Blind)":
			return new FuzzerSQLBlind(url);
		case "CSRF":
			return new FuzzerCSRF(url);
		}
		return null;
	}
}
