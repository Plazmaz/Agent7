package me.dylan.Agent7.gui;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.res.ContentLoader;

public class FrameMain extends JFrame{
	private static final long serialVersionUID = 7703680334366914852L;
	public FrameMain() {
		super("Agent7 v"+Agent7.version);
		setResizable(false);
		try {
			this.setContentPane(new JLabel(new ImageIcon(ContentLoader.getImageFromInternalFile("Agent7.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
