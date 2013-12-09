package me.dylan.Agent7.http.Fuzzer;

import java.util.ArrayList;

import org.jsoup.Connection;

public interface IFuzzer {
	/**
	 * Make a test connection to look for signs of a successful attack
	 * 
	 * @param the
	 *            list of all GET/POST request parameters
	 */
	public abstract void executeTestConnection(ArrayList<String> params);

	/**
	 * Send specified payload through the connection on all parameter fields.
	 * I'M GIVING 'ER ALL SHE'S GOT CAPTAIN!
	 * 
	 * @param connection
	 *            The connection to send data through
	 * @param payload
	 *            The payload we're attempting on fields
	 */
	public abstract void sendGetPostPayloads(Connection connection,
			String payload);

	/**
	 * Pretty self-explanatory. Gather all of the forms on the current page.
	 */
	public abstract void gatherAllFormIds();

	/**
	 * Send the initial get request for receiving a reference page(to
	 * differentiate from) and also pinging the web server.
	 */
	public abstract void sendInitialRequest();

	/**
	 * This will fire all attack methods possible for the given attack type
	 */
	public abstract void initializeAttack();

	public abstract void setUrl(String url);
	/**
	 * This was implemented to give some structure.
	 * Simply logs [friendlyName]info
	 * @param info
	 */
	public abstract void info(String info);

	/**
	 * Similar to info, logs [friendlyName]exception.getMessage();
	 * @param exception
	 */
	public abstract void err(Exception exception);
}
