package me.dylan.Agent7.gui;

import java.awt.Color;

import javax.swing.JMenu;

public class ProperMenu extends JMenu {
	private static final long serialVersionUID = -4213974369375340321L;
	public ProperMenu(String label) {
		super(label);
		this.setBackground(Color.BLACK);
		this.setForeground(Color.RED);
		this.setOpaque(true);
	}

}
