package me.dylan.Agent7.Threads;

import java.awt.HeadlessException;
import java.io.FileNotFoundException;


import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.dictionary.DictionaryFileUtil;

public class ThreadGetResultsBFLocalMulti extends Thread {
	int threadsFinished = 0;
	ThreadBruteforce[] allThreads;
	String result = "";

	public ThreadGetResultsBFLocalMulti(ThreadBruteforce[] threads) {
		allThreads = threads.clone();
	}

	public void run() {
		result = "";
		while (threadsFinished < allThreads.length) {
			int count = 0;
			for (int i = 0; i < allThreads.length; i++) {
				count += allThreads[i].getComparisons();
				if (i >= threadsFinished && allThreads[i].getVerified()) {
					threadsFinished++;
				}
			}
			if (count % 1000 == 0) {
				Agent7.logLine("Passwords Attempted: " + count);
				double percentFinished = ((count /Math.pow(ThreadBruteforce.posLength,
						allThreads[0].getPassLength())) * 100);
				Agent7.setProgress((int) Math.ceil(percentFinished));
			}
			try {
				Thread.sleep(0, 5);
			} catch (InterruptedException e) {
				Agent7.err(e);
			}
		}
		Agent7.logLine("Retreving Password...");
		try {
			Thread.sleep(5);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (ThreadBruteforce brute : allThreads) {
			if (brute instanceof ThreadBruteforceFTP)
				result += brute.constructCurrentString();
			else {
				result += brute.constructCurrentString();
			}
		}
		Agent7.logLine("Password: " + result);
		DictionaryFileUtil util = new DictionaryFileUtil("Dictionary.txt");
		try {
			if (!Agent7.instance.resLoader.searchForWord(result)
					&& !util.search(result)) {
				/*int canSave = JOptionPane
						.showConfirmDialog(
								null,
								"Would you like to contribute to this expirement by saving your password? (We couldn't hack you if we wanted to, no username!)",
								"Contribute Password?", JOptionPane.OK_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				if (canSave == JOptionPane.OK_OPTION) {
					util.write(result);
					Agent7.logLine("Thanks! That password tastes good!");
				}*/

			}
		} catch (HeadlessException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
