package me.dylan.Agent7.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;

import me.dylan.Agent7.Agent7;

public class FrameFuzzer extends ModularFrame {

	private static final long serialVersionUID = -6767156220174748902L;
	ProperTextField url = new ProperTextField("");
	ArrayList<JCheckBox> attackOptions = new ArrayList<JCheckBox>();
	public static ProperTextField useragent = new ProperTextField("");
	JCheckBox crawl = new JCheckBox("Crawl?");
	ProperButton cookie = new ProperButton("Set a cookie");
	ProperButton go = new ProperButton("Go!");
	JSpinner threads = new JSpinner();
	private FrameFuzzer instance;
	
	public FrameFuzzer(String name) {
		super(name);
		instance = this;
	}

	public void init() {
		initAttackOptions();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		JPanel strParamsPanel = new JPanel();
		strParamsPanel.setBackground(Colors.inputBackground1);
		int rows = 3;
		int cols = 1;
		strParamsPanel.setLayout(new GridLayout(rows, cols));
		JLabel label = new JLabel("URL: ");
		label.setForeground(Colors.infoColor);
		strParamsPanel.add(label);
		strParamsPanel.add(url);
		JLabel userAgentLabel = new JLabel("User-Agent: ");
		userAgentLabel.setForeground(Colors.infoColor);
		strParamsPanel.add(userAgentLabel);
		strParamsPanel.add(useragent);
		for (int i = 0; i < (rows * cols) - 4; i++) {
			strParamsPanel.add(Box.createHorizontalBox());
			strParamsPanel.add(Box.createVerticalBox());
		}

		this.add(strParamsPanel);

		JPanel options = new JPanel();
		options.setBackground(Colors.inputBackground1);
		options.setLayout(new GridLayout(2, 3));
		TitledBorder border = BorderFactory
				.createTitledBorder("Attack Methods");
		border.setTitleColor(Colors.titlesColor);
		options.setBorder(border);
		for (JCheckBox box : attackOptions) {
			box.setBackground(Colors.inputBackground1);
			box.setForeground(Colors.inputColor);
			options.add(box);
		}
		
		cookie.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				String sesCookie = (String) JOptionPane.showInputDialog(instance,
						"Please enter cookie", "Cookie: ",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (sesCookie == null || sesCookie.isEmpty() || !sesCookie.contains(":")) {
					JOptionPane.showMessageDialog(instance,
							"Invalid cookie format. Please use key:value.",
							"Invalid Cookie Format", JOptionPane.ERROR_MESSAGE);
				} else {
					Agent7.cookies.put(sesCookie.split(":")[0],
							sesCookie.split(":")[1]);
					Agent7.useCookies = true;
				}
			}
		});
		
		options.add(cookie);
		this.add(options);

		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout());
		controls.setBackground(Colors.infoBackground1);
		crawl.setBackground(Colors.inputBackground1);
		crawl.setForeground(Colors.inputColor);
		crawl.setEnabled(true);
//		threads.setValue(1);
//		JLabel inf = new JLabel("Thread Count: ");
//		inf.setBackground(Colors.inputBackground1);
//		inf.setForeground(Colors.inputColor);
		controls.add(crawl);
//		controls.add(inf);
//		controls.add(threads);
		go.addActionListener(new FuzzSubmitActionListener(this));
		controls.add(go);
		this.add(controls);
	}

	private void initAttackOptions() {
		attackOptions.add(new JCheckBox("SQL Injection(Errors)"));
		attackOptions.add(new JCheckBox("XSS(Script Injection)"));
		attackOptions.add(new JCheckBox("SQL Injection(Blind)"));
		JCheckBox php = new JCheckBox("PHP Injection(SLOW)");
		attackOptions.add(php);
		attackOptions.add(new JCheckBox("CSRF"));
		for (int i = 0; i < 4; i++) {
			attackOptions.get(i).setSelected(true);
		}
	}

}
