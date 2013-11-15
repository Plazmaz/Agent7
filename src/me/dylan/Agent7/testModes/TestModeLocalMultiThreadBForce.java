package me.dylan.Agent7.testModes;

import java.util.ArrayList;

import me.dylan.Agent7.Agent7;
import me.dylan.Agent7.Threads.ThreadBruteforce;
import me.dylan.Agent7.Threads.ThreadBruteforceLocal;
import me.dylan.Agent7.Threads.ThreadGetResultsBFLocalMulti;

public class TestModeLocalMultiThreadBForce implements TestMode {

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
		return "Multi-Threaded Bruteforce";
	}

	public void performTest(String password, int threadCount) {
		if (password.length() % threadCount != 0) {
			throw new IllegalArgumentException("Invalid thread count."
					+ " The password must be(in this version) divisible by "
					+ "the thread count.");
		}
		int pieceSize = (password.length() / threadCount);
		for (int i = 0; i < threadCount; i++) {
			int trueIndex = i * pieceSize;
			String passwordToWorkOn = password.substring(trueIndex, trueIndex
					+ pieceSize);
			ThreadBruteforceLocal bforce = new ThreadBruteforceLocal(
					pieceSize, passwordToWorkOn);
			if(Agent7.consumeAllPossibleRes) {
				bforce.setDelayMiliS(0);
				bforce.setDelayMicroS(0);
			}
			bforce.start();
			threads.add(bforce);
		}
		ThreadBruteforce[] bruteforceThs = new ThreadBruteforce[threads.size()];
		for (Thread force : threads) {
			bruteforceThs[threads.indexOf(force)] = (ThreadBruteforceLocal) force;
		}
		ThreadGetResultsBFLocalMulti results = new ThreadGetResultsBFLocalMulti(
				bruteforceThs);
		results.start();
		threads.add(results);

	}

	@Override
	/**
	 * Will perform test as normal, with two threads.
	 */
	public void performTest(String password) {
		performTest(password, 2);
	}
}
