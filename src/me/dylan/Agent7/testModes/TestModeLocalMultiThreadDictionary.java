package me.dylan.Agent7.testModes;

import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadDictionaryLocal;

public class TestModeLocalMultiThreadDictionary implements TestMode {
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
		return "Multi-Threaded Local Dictionary Attack";
	}

	public void performTest(int threadCount, String password) {
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
			ThreadDictionaryLocal dictionary = null;
			dictionary = new ThreadDictionaryLocal(password, i);
			dictionary.start();
			threads.add(dictionary);
		}
		ThreadDictionaryLocal[] dictThreads = new ThreadDictionaryLocal[threads
				.size()];
		for (Thread force : threads) {
			dictThreads[threads.indexOf(force)] = (ThreadDictionaryLocal) force;
		}
		// ThreadGetResults results = new ThreadGetResults(bruteforceThs);
		// results.start();
		// threads.add(results);

	}

	@Override
	/**
	 * Will perform test as normal, with two threads.
	 */
	public void performTest(String password) {
		performTest(2, password);
	}
}
