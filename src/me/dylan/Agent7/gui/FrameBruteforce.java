package me.dylan.Agent7.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import me.dylan.Agent7.testModes.TestType;

public class FrameBruteforce extends ModularFrame {
	private static final long serialVersionUID = 238573555066179833L;
	JSpinner threads = new JSpinner();
	/**
	 * I know it's named local radio, but I assure you, it has good stations.
	 */
	public TestType mode;
	JRadioButton localradio = new JRadioButton("Local(Enter Password)");
	JRadioButton ftpradio = new JRadioButton("FTP(Log in to FTP)");
	public JPasswordField bfPass = new JPasswordField("");
	ProperButton submitData = new ProperButton("Go!");
	JTextField bfPort = new JTextField();

	public FrameBruteforce(String name) {
		super(name);
	}

	public void init() {
		ButtonGroup group = new ButtonGroup();
		group.add(localradio);
		group.add(ftpradio);
		this.setLayout(new GridLayout(4, 2));

		JPanel attackMethods = new JPanel();
		attackMethods.setBackground(Colors.inputBackground1);
		// attackMethods.setForeground(fg);
		TitledBorder border = BorderFactory.createTitledBorder("Attack Method");
		border.setTitleColor(Colors.titlesColor);
		attackMethods.setBorder(border);
		attackMethods.setLayout(new GridLayout(2, 0));
		
		localradio.setBackground(Colors.inputBackground1);
		localradio.setForeground(Colors.inputColor);
		attackMethods.add(localradio);
		bfPass.setForeground(Colors.inputColor);
		bfPass.setOpaque(false);
		attackMethods.add(bfPass);
		
		ftpradio.setBackground(Colors.inputBackground1);
		ftpradio.setForeground(Colors.inputColor);
		attackMethods.add(ftpradio);
		bfPort.setForeground(Colors.inputColor);
		bfPort.setOpaque(false);
		attackMethods.add(bfPort);
		this.add(attackMethods);

		JPanel extrainfosubmit = new JPanel();
		extrainfosubmit.setLayout(new GridLayout(2, 1));
		extrainfosubmit.setBackground(Colors.infoBackground1);

		JPanel submitSizer = new JPanel();
		submitSizer.setBackground(Colors.inputBackground1);
		submitSizer.setLayout(new GridLayout(1, 5));
		JLabel inf = new JLabel("Thread Count");
//		inf.setForeground(Colors.infoColor);
		submitSizer.add(inf);

		// threads.setValue(Math.abs(threads.getValue())); - TODO: Put that in a
		// loop/thread.
		submitSizer.add(threads);
		submitSizer.add(Box.createHorizontalBox());
		submitSizer.add(Box.createHorizontalBox());
		submitSizer.add(submitData);
		// Inception.
		extrainfosubmit.add(submitSizer);
		this.add(extrainfosubmit);
		initActionListeners();
	}

	public void initActionListeners() {
		submitData.addActionListener(new BruteforceActionListener(this));
//		final Color selected = new Color(255, 0, 0);
		
		ftpradio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				mode = TestType.FTP;
//				bfPass.setFocusable(false);
//				bfPort.setFocusable(true);
//				bfPass.setBackground(Color.WHITE);
//				bfPort.setBackground(selected);
//				localradio.setBackground(Color.BLACK);
//				ftpradio.setBackground(selected);
//				bfPass.setFocusable(false);
			}
			
		});
		localradio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				mode = TestType.LOCAL;
//				bfPass.setFocusable(true);
//				bfPort.setFocusable(false);
//				bfPass.setBackground(selected);
//				bfPort.setBackground(Color.WHITE);
//				localradio.setBackground(selected);
//				ftpradio.setBackground(Color.BLACK);
			}
			
		});
	}
}
