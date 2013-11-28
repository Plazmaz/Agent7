package me.dylan.Agent7.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JMenuBar;

public class ProperMenubar extends JMenuBar {
	private static final long serialVersionUID = 2350083698381998111L;

	public ProperMenubar(ArrayList<Component> components) {
		super();
		 this.setBackground(Color.BLACK);
		 this.setForeground(Color.RED);
		for (Component c : components) {
			if (c instanceof ProperButton)
				add((ProperButton) c);
			else
				add(c);
		}
	}

}
