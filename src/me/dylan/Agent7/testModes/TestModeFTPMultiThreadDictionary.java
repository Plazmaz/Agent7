package me.dylan.Agent7.testModes;

import java.io.IOException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadDictionaryFTP;
import me.dylan.Agent7.Threads.ThreadDictionaryLocal;


public class TestModeFTPMultiThreadDictionary implements TestMode {
	private ArrayList<Thread> threads = new ArrayList<Thread>();
	public String result;

	@Override
	public void endTest() {
		for (Thread th : threads) {
			th.interrupt();
		}
	}

	@Override
	public String getTestName() {
		return "Multi-Threaded FTP Dictionary Attack";
	}

	public void performTest(int threadCount, String username, int port) throws IOException {
		int wordsSize = 0;
		wordsSize = Agent7.instance.resLoader.getAllFileContents().size();
		if (wordsSize % threadCount != 0) {
			Agent7.logLine("Word count, " + wordsSize
					+ " is not evenly divisible\nby thread count, "
					+ threadCount + "\nsome final passwords may be skipped!");
//			return;
		}
		int pieceSize = (wordsSize / threadCount);
		for (int i = 0; i < wordsSize; i += pieceSize) {
			ThreadDictionaryFTP dictionary = null;
			dictionary = new ThreadDictionaryFTP(i, port, username, this);
			dictionary.start();
			threads.add(dictionary);
		}

	}

	@Override
	/**
	 * Will perform test as normal, with two threads, and port 21
	 */
	public void performTest(String username) {
		try {
			performTest(2, username, 21);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
