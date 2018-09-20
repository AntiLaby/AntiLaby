package com.github.antilaby.antilaby.updater;

/**
 * Checks for AntiLaby updates. This class loads, caches and parses the update information.
 *  *
 * @author NathanNr
 */
public class UpdateChecker {

	/**
	 * The location of the online update information provider
	 */
	private String uri = "";

	public UpdateChecker(String uri) {
		this.uri = uri;
	}

	public UpdateInformation getUpdateInformation() {
		UpdateInformation updateInformation = new UpdateInformation();
		// TODO
		return updateInformation;
	}

}
