package me.dylan.Agent7.testModes;

import java.io.IOException;

import me.dylan.Agent7.Threads.ThreadBruteforceFTP;
@Deprecated
/**
 * Deprecated. Please use TestModeFTPMultiThreadBForce with a thread count of
 * one if you wish to use this class.
 */
public class TestModeFTPBruteforce implements TestMode {
	ThreadBruteforceFTP brute;
	@Override
	public void endTest() {
		brute = null;
	}

	@Override
	public String getTestName() {
		return "Ftp Bruteforce attack";
	}

	@Override
	public void performTest(String info) {
		try {
			brute = new ThreadBruteforceFTP(Integer.parseInt(info), "admin");
			brute.start();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

}
