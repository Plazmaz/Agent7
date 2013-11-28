package me.dylan.Agent7.gui;

import java.awt.Color;

import javax.swing.JFrame;

public class ModularFrame extends JFrame {
	private static final long serialVersionUID = 7409593252100133035L;

	public ModularFrame(String name) {
		super(name);
		this.getContentPane().setBackground(Color.BLACK);
		this.getContentPane().setForeground(Color.GREEN);
	}
}
