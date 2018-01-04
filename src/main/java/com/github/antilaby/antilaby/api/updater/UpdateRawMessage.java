package com.github.antilaby.antilaby.api.updater;

/**
 * Provides information about a raw update message.
 * 
 * @author NathanNr
 */

public class UpdateRawMessage {

	private String message;
	private UpdateRawMessageLocation loc;

	/**
	 * @param message
	 *            The update message
	 * @param loc
	 *            The location of the update message
	 */
	public UpdateRawMessage(String message, UpdateRawMessageLocation loc) {
		this.message = message;
		this.loc = loc;
	}

	/**
	 * @return The raw message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return The location of the raw message
	 */
	public UpdateRawMessageLocation getLoc() {
		return loc;
	}

}
