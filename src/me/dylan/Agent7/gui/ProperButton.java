package me.dylan.Agent7.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.testModes.TestMode;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadBForce;
import me.dylan.Agent7.testModes.TestModeLocalMultiThreadDictionary;
import me.dylan.Agent7.testModes.TestType;

/**
 * This is the beginning of a total UI Rewrite.
 * 
 */
public class ProperButton extends JButton {
	private static final long serialVersionUID = -5613675445790470261L;

	 public ProperButton(String label) {
		 super(label);
		 this.setOpaque(true);
		 this.setBackground(/*new Color(111, 111, 111)*/Color.DARK_GRAY);
		 this.setForeground(Color.RED);
		 this.setFocusable(false);
//		 this.setBorder(BorderFactory.createRaisedBevelBorder());
	 }
	
}
