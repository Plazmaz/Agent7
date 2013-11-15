package me.dylan.Agent7.testModes;

public interface TestMode {
	/**
	 * End all information sent by, read by, and
	 * all tasks performed by this test.
	 */
	public abstract void endTest();
	/**
	 * Get the user friendly name of this test.
	 * @return Test name
	 */
	public abstract String getTestName();
	/**
	 * Perform this test with the given info string. It will be parsed
	 * uniquely by each test.
	 * (i.e: FTP uses hostname:port, local uses password to test against)
	 * @param info
	 */
	void performTest(String info);
}
