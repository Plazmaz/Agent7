package me.dylan.Agent7.testModes;

import me.dylan.Agent7.Threads.ThreadDictionary;
import me.dylan.Agent7.Threads.ThreadDictionaryLocal;

@Deprecated
/**
 * Deprecated. Please use TestModeLocalMultiThreadDictionary with a thread count of
 * one if you wish to use this class.
 */
public class TestModeLocalDictionary implements TestMode {
	ThreadDictionary dict;
	@Override
	public void endTest() {
		dict.interrupt();
	}

	@Override
	public String getTestName() {
		return "Local Dictionary Attack";
	}

	@Override
	public void performTest(String comparator) {
		dict = new ThreadDictionaryLocal(comparator);
		dict.start();
	}

}
