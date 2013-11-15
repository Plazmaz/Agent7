package me.dylan.Agent7.testModes;

import me.dylan.Agent7.Threads.ThreadBruteforceLocal;
@Deprecated
/**
 * Deprecated. Please use TestModeLocalMultiThreadBForce with a thread count of
 * one if you wish to use this class.
 */
public class TestModeLocalBruteforce implements TestMode {
	String passToTest;
	ThreadBruteforceLocal crack1;

	@Override
	public void performTest(String password) {
		this.performTestWithEstimate(password, 0);
	}

	public void performTestWithEstimate(String pass, int length) {
		this.passToTest = pass;
		crack1 = new ThreadBruteforceLocal(length, passToTest);
		crack1.start();
	}

	@Override
	public void endTest() {
		if (crack1 != null)
			crack1 = null;
		
	}

	@Override
	public String getTestName() {
		return "Local Test";
	}

}
