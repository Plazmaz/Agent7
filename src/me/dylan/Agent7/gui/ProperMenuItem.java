package me.dylan.Agent7.gui;

import java.awt.Color;

import javax.swing.JMenuItem;

public class ProperMenuItem extends JMenuItem {
	private static final long serialVersionUID = 2351896986345601622L;
	public ProperMenuItem(String label) {
		super(label);
		this.setBackground(Color.BLACK);
		this.setForeground(Color.RED);
		this.setOpaque(true);
	}

}
