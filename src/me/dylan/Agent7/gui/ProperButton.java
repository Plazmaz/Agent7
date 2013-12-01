package me.dylan.Agent7.gui;

import javax.swing.JButton;

/**
 * This is the beginning of a total UI Rewrite.
 * 
 */
public class ProperButton extends JButton {
	private static final long serialVersionUID = -5613675445790470261L;

	 public ProperButton(String label) {
		 super(label);
		 this.setOpaque(true);
		 this.setBackground(Colors.inputBackground2);
		 this.setForeground(Colors.inputColor);
		 this.setFocusable(false);
//		 this.setBorder(BorderFactory.createRaisedBevelBorder());
	 }
	
}
