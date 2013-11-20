package me.dylan.Agent7.Threads;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.dictionary.DictionaryLoader;

public class ThreadDictionary extends Thread {
	DictionaryLoader resLoader;
	private int delayMicroS = 50;
	private int delayMiliS = 0;
	private String currentString = "";
	protected String comparator = "";
	private int comparisons = 0;
	private boolean isFinished;
	private int offset = 0;

	public ThreadDictionary(String comparator, int offset) {
		this.comparator = comparator;
		this.offset = offset;
		Agent7.logLine("Dictionary Words Loaded.");
	}

	@Override
	public void run() {
		while (!this.getVerified() && !this.isInterrupted()) {
			if (comparisons + offset < Agent7.instance.resLoader
					.getAllFileContents().size() - 1) {
				comparisons++;
				// if (comparisons % 1000 == 0) {
				Agent7.logLine("Attempted " + comparisons + " Passwords.");
				// }
				currentString = Agent7.instance.resLoader.getAllFileContents()
						.get(comparisons + offset);
				updateVerification();
				// if (!currentString.isEmpty())
			} else {
				Agent7.logLine("Your password was not found in our database! Good job!");
				break;
			}
			if (!Agent7.consumeAllPossibleRes) {
				try {
					Thread.sleep(getDelayMiliS(), getDelayMicroS());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int getComparisons() {
		return comparisons;
	}

	public void setComparisons(int comparisons) {
		this.comparisons = comparisons;
	}

	public int getDelayMicroS() {
		return delayMicroS;
	}

	public void setDelayMicroS(int delayMicroS) {
		this.delayMicroS = delayMicroS;
	}

	public void updateVerification() {
	}

	public int getDelayMiliS() {
		return delayMiliS;
	}

	public void setDelayMiliS(int delayMiliS) {
		this.delayMiliS = delayMiliS;
	}

	public void setVerified(boolean verify) {
		this.isFinished = verify;
	}

	public boolean getVerified() {
		return this.isFinished;
	}

	public String getCurrentString() {
		return currentString;
	}

	public void setCurrentString(String currentString) {
		this.currentString = currentString;
	}
}
