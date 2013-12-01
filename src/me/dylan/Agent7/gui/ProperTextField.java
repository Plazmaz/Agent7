package me.dylan.Agent7.gui;

import javax.swing.JTextField;

public class ProperTextField extends JTextField{
	private static final long serialVersionUID = -841205844244734536L;
	public ProperTextField(String content) {
		super(content);
		this.setBackground(Colors.inputBackground1);
		this.setForeground(Colors.inputColor);
	}

	
}
