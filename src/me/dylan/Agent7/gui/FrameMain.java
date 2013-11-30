package me.dylan.Agent7.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.res.ContentLoader;

public class FrameMain extends JFrame{
	private static final long serialVersionUID = 7703680334366914852L;
	/*
	 * information elements
	 */
	JTextArea output;
	JScrollPane scroll;
	public static JProgressBar progressInCurrentTask = new JProgressBar();
	public static int percentageCompleteCurrentTask = 0;
	JLabel currentTask = new JLabel("Awaiting Commands...");
	public ArrayList<String> logQue = new ArrayList<String>();
	
	/*
	 * input elements
	 */
	public ProperButton localTestButton;
	public ProperButton ftpTestButton;
	public ProperButton fuzzTestButton;
	public FrameMain() {
		super("Agent7 v"+Agent7.version);
		setResizable(false);
		try {
			this.setContentPane(new JLabel(new ImageIcon(ContentLoader.getImageFromInternalFile("Agent7.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void init() {
		initButtons();
		this.setSize(700, 630);
		progressInCurrentTask.setForeground(Color.RED);
		progressInCurrentTask.setBackground(Color.BLACK);
		progressInCurrentTask.setBorderPainted(false);
		progressInCurrentTask.setStringPainted(true);
		output = new JTextArea(10, 65);
		output.setBackground(Color.BLACK);
		output.setForeground(Color.GREEN);
		output.setLineWrap(true);
		// TODO: Replace with a custom class(maybe.)
		output.setEditable(false);
		scroll = new JScrollPane(output);
		scroll.setFont(new Font("Helvetica", Font.ITALIC, 12));
		scroll.setAutoscrolls(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel buttons = new JPanel();
		buttons.setLocation(getWidth()/4, getHeight()/8);
		buttons.setBackground(Color.black);
		buttons.setBorder(BorderFactory.createBevelBorder(2));
		buttons.setSize(new Dimension(getWidth() / 2, (getHeight() / 8)*6));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
//		buttons.add(Box.createRigidArea(new Dimension(getWidth()/4,getWidth()/4)));
		// constraints.gridwidth = 80;
		// constraints.gridheight = 50;
//		constraints.weightx = 1;
//		constraints.weighty = 1;
//		constraints.gridx = 0;
//		constraints.gridy = 0;
//		constraints.anchor = GridBagConstraints.NORTH;
//		constraints.insets = new Insets(100, 0, 0, 0);
		localTestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(localTestButton);
		ftpTestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(ftpTestButton);

		//constraints.insets = new Insets(-70, 0, 0, 0);
		//constraints.gridx = 0;
		//constraints.gridy = 2;
		fuzzTestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(fuzzTestButton);
		currentTask.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(currentTask);
		progressInCurrentTask.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(progressInCurrentTask);

		scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.add(scroll);
		this.add(buttons, BorderLayout.CENTER);
		// this.add(scrapeWords, this);
		this.setVisible(true);
		beginGUIUpdates();
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

						if (getContentPane().getCursor().getType() != Cursor.WAIT_CURSOR) {
							getContentPane().setCursor(
									new Cursor(Cursor.WAIT_CURSOR));
						}
					} else {
						if (getContentPane().getCursor().getType() != Cursor.DEFAULT_CURSOR) {
							getContentPane().setCursor(
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
	private void initButtons() {
		localTestButton = new ProperButton("Brute force Tests");
		ftpTestButton = new ProperButton("Dictionary Tests");
		fuzzTestButton = new ProperButton("Web/Fuzzing Tests");

	}
	public static void setTask(String task) {
		Agent7.instance.menu.currentTask.setText(task);
	}
}
