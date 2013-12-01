package me.dylan.Agent7.gui;

import javax.swing.JMenu;

public class ProperMenu extends JMenu {
	private static final long serialVersionUID = -4213974369375340321L;
	public ProperMenu(String label) {
		super(label);
		this.setBackground(Colors.inputBackground1);
		this.setForeground(Colors.inputColor);
		this.setOpaque(true);
	}

}
