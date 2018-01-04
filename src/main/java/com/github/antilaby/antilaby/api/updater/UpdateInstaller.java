package com.github.antilaby.antilaby.api.updater;

import com.github.antilaby.antilaby.log.Logger;

/**
 * Installs an update. It is highly recommended to run this task in another
 * thread and not in the main thread!
 * 
 * @author NathanNr
 */

public class UpdateInstaller {

	private final Logger LOG = new Logger("UpdateInstaller");

	private Update update;

	public UpdateInstaller(Update update) {
		this.update = update;
	}

	public Update getUpdate() {
		return this.update;
	}

	public void install() {
		LOG.info("Downloading update to version " + update.getUpdateVersion() + "...");
		if (update.getUpdateShortMessage() != null)
			LOG.info("Update message: " + update.getUpdateShortMessage());
		else
			LOG.debug("No update message has been provided.");
		LOG.debug("Update download URL: " + update.getUpdateUrl());
		// TODO: Download update
		LOG.info("Download finished! Installing update...");
		// TODO: Install update
		LOG.info("The update to version " + update.getUpdateVersion()
				+ " has been finished. Please reload or restart you server to enable the new version!");
	}

}
