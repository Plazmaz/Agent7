package me.dylan.Agent7.Threads;

import java.math.BigInteger;

import me.dylan.Agent7.Agent7;

public class ThreadBruteforce extends Thread {
	public static char[] possibilities = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_!@#$"
			.toCharArray();
	static int posLength = possibilities.length;
	private int curlength;
	int[] current;
	String currentPasswordTesting = " ";
	int[] testpass;
	private int delayMicroS = 50;
	private int delayMiliS = 0;
	boolean isFinished;
	private long comparisons;
	private int length;

	public ThreadBruteforce(int passLength) {
		this.setPassLength(passLength);
		curlength = getPassLength();
		current = new int[curlength];

		BigInteger combos = BigInteger.valueOf((long) Math.pow(posLength,
				curlength));
		Agent7.logLine("Performing Brute Force Attack...");
		Agent7.logLine("Universe size: " + posLength);
		Agent7.logLine("Slots: " + curlength);
		Agent7.logLine("Possible values: " + combos);

	}

	/**
	 * Gets the next possible password for the given length.
	 * 
	 * @param length
	 *            - Simply used to calculate the possible values, does not need
	 *            to be precise. It can even be zero(but permutation
	 *            calculations will come out incorrectly.)
	 * @param offest
	 *            - The offset(for multi threaded ftp currently)
	 * 
	 * @return password - The next possible password
	 */
	public String getNextPass(int offset, int length) {
		if (!this.getVerified() && increment(0, offset)) {
			setComparisons(getComparisons() + 1);
			String s = new String(constructCurrentString());
			return s;

		} else {

			if (!getVerified())
				throw new IllegalArgumentException(

						"All possible permutations within given length tested. This most likely means no verification was provided or the password is longer than the given length.");

			return constructCurrentString();
		}
	}

	public boolean increment(int index, int offset) {
		if (index >= getPassLength()) {
			return false;
		} else {
			if (current[index] >= posLength) {
				current[index] = offset;
				index++;
				return increment(index, offset);
			} else {
				current[index]++;
			}
			return true;
			// }
		}
	}

	/*
	 * public boolean increment(int index, int offset) { if (index >= curlength)
	 * { return false; } else { if (current[index] >= posLength) {
	 * current[index] = offset; return increment(index + 1, offset); } else {
	 * current[index]++; return true; } } }
	 */
	public void setVerified(boolean verify) {
		this.isFinished = verify;
	}

	public boolean getVerified() {
		return this.isFinished;
	}

	public String constructCurrentString() {
		String curStr = "";
		for (int i = 0; i < current.length; i++) {
			int number = current[i];
			if (number < posLength
					&& !Character.isWhitespace(possibilities[number]))
				curStr += possibilities[number];
		}
		return curStr;
	}

	public void run() {
		while (!this.isFinished && !this.isInterrupted()) {
			currentPasswordTesting = getNextPass(getPassLength(), 0);
			try {
				Thread.sleep(getDelayMiliS(), getDelayMicroS());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setDelayMicroS(int delayMicroS) {
		this.delayMicroS = delayMicroS;
	}

	public long getComparisons() {
		return comparisons;
	}

	public void setComparisons(long comparisons) {
		this.comparisons = comparisons;
	}

	public int getDelayMicroS() {
		return delayMicroS;
	}

	public int getPassLength() {
		return length;
	}

	public void setPassLength(int length) {
		this.length = length;
	}

	public int getDelayMiliS() {
		return delayMiliS;
	}

	public void setDelayMiliS(int delayMiliS) {
		this.delayMiliS = delayMiliS;
	}
}
