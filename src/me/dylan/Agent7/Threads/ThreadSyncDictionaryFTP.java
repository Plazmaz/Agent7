package me.dylan.Agent7.Threads;

import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadSyncDictionaryFTP {
	public static AtomicBoolean finished = new AtomicBoolean(false);
	public static synchronized void finish() {
		finished.set(true);
	}
	public static synchronized boolean getFinished() {
		return finished.get();
	}
}
