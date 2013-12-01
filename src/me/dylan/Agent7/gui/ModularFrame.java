package me.dylan.Agent7.gui;

import javax.swing.JFrame;

public class ModularFrame extends JFrame {
	private static final long serialVersionUID = 7409593252100133035L;

	public ModularFrame(String name) {
		super(name);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(500, 400);
		this.getContentPane().setBackground(Colors.infoBackground1);
		this.getContentPane().setForeground(Colors.infoColor);
	}
}
