package me.dylan.Agent7.Threads;

public class ThreadBruteforceLocal extends ThreadBruteforce {
	String comparator;
	public ThreadBruteforceLocal(int passLength, String comparator) {
		super(passLength);
		this.comparator = comparator;
	}
	@Override
	public String getNextPass(int length, int offset) {
		String s = super.getNextPass(length, offset);
		if(s.equals(comparator)) {
			this.setVerified(true);
			this.currentPasswordTesting = s;
		}
		return s;
	}
	

}
