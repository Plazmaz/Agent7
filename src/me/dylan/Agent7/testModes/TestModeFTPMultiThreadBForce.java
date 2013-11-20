package me.dylan.Agent7.testModes;

import java.io.IOException;
import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadBruteforce;
import me.dylan.Agent7.Threads.ThreadBruteforceFTP;

public class TestModeFTPMultiThreadBForce implements TestMode {
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
		return "Multi-Threaded FTP Bruteforce";
	}

	public void performTest(int threadCount, String port, String username) {
//		int maxPassSize = ThreadBruteforce.possibilities.length;
		
		for (int i = 0; i < threadCount; i++) {
			ThreadBruteforceFTP ftp = null;
			try {
				ftp = new ThreadBruteforceFTP(Integer.parseInt(port), username);
			} catch (IOException e) {
				Agent7.err(e);
				e.printStackTrace();
				continue;
			}
			ftp.start();
			threads.add(ftp);
		}
//		ThreadBruteforceFTP[] bruteforceThs = new ThreadBruteforceFTP[threads.size()];
//		for (Thread force : threads) {
//			bruteforceThs[threads.indexOf(force)] = (ThreadBruteforceFTP) force;
//		}
//		ThreadGetResults results = new ThreadGetResults(bruteforceThs);
//		results.start();
//		threads.add(results);

	}

	@Override
	/**
	 * Will perform test as normal, with two threads.
	 */
	public void performTest(String username) {
		performTest(2, "21", username);
	}

}
