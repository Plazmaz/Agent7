package me.dylan.Agent7.Threads;

import java.io.File;

import javax.swing.JOptionPane;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.dictionary.DictionaryFileUtil;

public class ThreadDictionaryLocal extends ThreadDictionary {

	public ThreadDictionaryLocal(String comparator, int index) {
		super(comparator, index);
		// TODO Auto-generated constructor stub
	}

	public ThreadDictionaryLocal(String comparator) {
		super(comparator, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		super.run();
		if (!ThreadSyncDictionaryLocal.getFinished()) {
			Agent7.logLine("Found Password: " + getCurrentString());
			ThreadSyncDictionaryLocal.finish();
			for (File f : Agent7.instance.resLoader.getCurrentWords().keySet()) {
				DictionaryFileUtil futil = new DictionaryFileUtil(f.getName());
				Agent7.logLine(f.getName());
				if (futil.search(getCurrentString()))
					return;
			}

			int canSave = JOptionPane
					.showConfirmDialog(
							null,
							"Save Password?(We don't know your username, or even what you're using your password for, so we couldn't hack you"
									+ "even if we wanted to.)",
							"Would you like to contribute to this expirement by saving your password?",
							JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (canSave == JOptionPane.OK_OPTION) {
				DictionaryFileUtil util = new DictionaryFileUtil(
						"Dictionary.txt");
				util.write(getCurrentString());
				Agent7.logLine("Thanks! That password tastes good!");
			}
		}
	}

	@Override
	public void updateVerification() {
		if (getCurrentString().equals(comparator))
			this.setVerified(true);
	}
}
