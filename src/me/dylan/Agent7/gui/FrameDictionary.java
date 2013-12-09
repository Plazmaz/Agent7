package me.dylan.Agent7.gui;

public class FrameDictionary extends FrameBruteforce {

	private static final long serialVersionUID = 1482134909554495814L;

	public FrameDictionary(String name) {
		super(name);
	}

	@Override
	public void initActionListeners() {
		submitData.addActionListener(new DictionaryActionListener(this));
	}

}
