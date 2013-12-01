package me.dylan.Agent7.gui;

import javax.swing.JMenuItem;

public class ProperMenuItem extends JMenuItem {
	private static final long serialVersionUID = 2351896986345601622L;
	public ProperMenuItem(String label) {
		super(label);
		this.setBackground(Colors.inputBackground1);
		this.setForeground(Colors.inputColor);
		this.setOpaque(true);
	}

}
