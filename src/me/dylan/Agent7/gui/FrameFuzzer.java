package me.dylan.Agent7.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;

public class FrameFuzzer extends ModularFrame {

	private static final long serialVersionUID = -6767156220174748902L;
	ProperTextField url = new ProperTextField("");
	ArrayList<JCheckBox> attackOptions = new ArrayList<JCheckBox>();
	JCheckBox crawl = new JCheckBox("Crawl?");
	ProperButton go = new ProperButton("Go!");
	JSpinner threads = new JSpinner();
	public FrameFuzzer(String name) {
		super(name);
	}

	public void init() {
		initAttackOptions();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		JPanel urlPanel = new JPanel();
		urlPanel.setBackground(Colors.inputBackground1);
		int rows = 3;
		int cols = 1;
		urlPanel.setLayout(new GridLayout(rows, cols));
		JLabel label = new JLabel("URL: ");
		label.setForeground(Colors.infoColor);
		urlPanel.add(label);
		urlPanel.add(url);
		for(int i=0; i < (rows*cols)-2; i++) {
			urlPanel.add(Box.createHorizontalBox());
			urlPanel.add(Box.createVerticalBox());
		}
		
		
		this.add(urlPanel);
		
		JPanel options = new JPanel();
		options.setBackground(Colors.inputBackground1);
		options.setLayout(new GridLayout(2, 3));
		TitledBorder border = BorderFactory.createTitledBorder("Attack Methods");
		border.setTitleColor(Colors.titlesColor);
		options.setBorder(border);
		for(JCheckBox box : attackOptions) {
			box.setBackground(Colors.inputBackground1);
			box.setForeground(Colors.inputColor);
			options.add(box);
		}
		this.add(options);
		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout());
		controls.setBackground(Colors.infoBackground1);
		crawl.setBackground(Colors.inputBackground1);
		crawl.setForeground(Colors.inputColor);
		threads.setValue(1);
		JLabel inf  = new JLabel("Thread Count: ");
		inf.setBackground(Colors.infoBackground1);
		inf.setForeground(Colors.infoColor);
		controls.add(crawl);
		controls.add(inf);
		controls.add(threads);
		controls.add(go);
		this.add(controls);
	}

	private void initAttackOptions() {
		attackOptions.add(new JCheckBox("SQL Injection(Errors)"));
		attackOptions.add(new JCheckBox("PHP Injection"));
		attackOptions.add(new JCheckBox("XSS(Script Injection)"));
		attackOptions.add(new JCheckBox("SQL Injection(Blind)"));
		attackOptions.add(new JCheckBox("Overflow(Integer)"));
		attackOptions.add(new JCheckBox("Overflow(Long)"));
		for(int i=0; i < 4; i++) {
			attackOptions.get(i).setSelected(true);
		}
	}

}
