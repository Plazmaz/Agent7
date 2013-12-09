package me.dylan.Agent7.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.dictionary.WebCrawler.CrawlerTendril;
import me.dylan.Agent7.http.Fuzzer.FuzzerCSRF;
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
			@Override
			public void run() {
				ArrayList<IFuzzer> tests = new ArrayList<IFuzzer>();
				for (JCheckBox box : parent.attackOptions) {
					if (box.isSelected()) {
						final IFuzzer fuzzer = initializeTestForBox(box,
								parent.url.getText());
						if (!parent.crawl.isSelected()) {
							fuzzer.initializeAttack();
						} else {
							tests.add(fuzzer);
						}
					}
				}
				Agent7.results.init();
				if(tests.size() > 0) {
					CrawlerTendril tendril = new CrawlerTendril(parent.url.getText(), tests);
					tendril.runGrowingThread();
				}
			}
		}).start();
	}

	private IFuzzer initializeTestForBox(JCheckBox box, String url) {
		switch (box.getText()) {
		case "SQL Injection(Errors)":
			return new FuzzerSQL(url);
		case "PHP Injection(SLOW)":
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
