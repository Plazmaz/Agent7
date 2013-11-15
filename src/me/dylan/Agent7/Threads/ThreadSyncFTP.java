package me.dylan.Agent7.Threads;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import me.dylan.Agent7.Agent7;

public class ThreadSyncFTP {
	
	/**
	 * The offset from which to start
	 */
	public static AtomicInteger offset = new AtomicInteger(0);
	/**
	 * The count of passwords tested
	 */
	public static AtomicInteger passwordsTested = new AtomicInteger(0);
	/**
	 * The array of all indices for the character array
	 */
	public static AtomicIntegerArray current;
	public static synchronized String getNextPass(
			ThreadBruteforceFTP threadBruteforceFTP) {
		String pass = "";
		offset = new AtomicInteger(threadBruteforceFTP.offset);
		// if(passwordsTested % 100 == 0) {
		// }
//		index.incrementAndGet();
		if (current == null) {
			current = new AtomicIntegerArray(
					threadBruteforceFTP.getPassLength());
			for (int i = 0; i < current.length(); i++) {
				current.set(i, -1);
			}
		}
		increment(new AtomicInteger(0), offset.get(), threadBruteforceFTP);
		return pass;
	}

	public static synchronized boolean increment(AtomicInteger currentlyWorking, int offset,
			ThreadBruteforce thread) {
		String pass = thread.constructCurrentString();
		Agent7.logLine("Current Password: " + pass);
		if (currentlyWorking.intValue() >= thread.getPassLength()) {
			return false;
		} else {
			passwordsTested.incrementAndGet();
			if (current.get(currentlyWorking.intValue()) >= ThreadBruteforce.posLength - 1) {
				current.set(currentlyWorking.intValue(), offset);
//				index.getAndSet(offset);
				currentlyWorking.getAndIncrement();
				return increment(new AtomicInteger(currentlyWorking.get()+1), offset, thread);
			} else {
				int value = current.get(currentlyWorking.intValue());
//				System.out.println("Current value: " + value + " At Index"
//						+ currentlyWorking.intValue());
				current.set(currentlyWorking.intValue(), value + 1);
			}
			return true;
			// }
		}
	}

	public static String constructCurrentString() {
		String curStr = "";
		for (int i = 0; i < current.length() - 1; i++) {
			int number = current.get(i);
			if (number < ThreadBruteforce.posLength
					&& number != -1)
				curStr += ThreadBruteforce.possibilities[number];
		}
		return curStr;
	}
}
