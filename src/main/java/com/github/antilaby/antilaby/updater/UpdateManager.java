package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.api.config.ConfigReader;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Manage the auto-update process
 *
 * @author NathanNr
 */
public class UpdateManager extends Thread {

	private static final Logger logger = new Logger("UpdateManager");

	private ConfigReader configReader;

	private boolean autoUpdate;
	private boolean includeBeta;
	private boolean includeTest;

	private final String temporaryFileLocation = "plugins/AntiLaby/tmp/AntiLaby.tmp";

	public UpdateManager() {
		// Get update information from the configuration file
		this.configReader = new ConfigReader();
		this.autoUpdate = configReader.getAutoUpdate().release();
		this.includeBeta = configReader.getAutoUpdate().beta();
		this.includeTest = configReader.getAutoUpdate().test();
	}

	@Override
	public void run() {
		// Checking for updates
		if (autoUpdate == false) {
			logger.info("Auto-update has been disabled in the configuration file.");
			return;
		}
		logger.debug("Checking for new updates...");
		UpdateInformation updateInformation;
		try {
			if (!includeTest) {
				if (!includeBeta) {
					updateInformation = check(UpdateInformationType.RELEASE);
				} else {
					updateInformation = check(UpdateInformationType.BETA);
				}
			} else {
				updateInformation = check(UpdateInformationType.TEST);
			}
		} catch (IOException e) {
			logger.warn("Failed to check for updates: Network error");
			return;
		} catch (ParseException e) {
			logger.warn("Failed to check for updates: Parsing error");
			return;
		}
		if (updateInformation == null) {
			logger.warn("Failed to check for updates.");
			return;
		}
		// Check if a newer version is available; cancel the update process, if the most recent version is already installed
		if (updateInformation.getVersionId() <= Constants.VERSION_ID) {
			logger.info("The most recent version is already installed.");
			return;
		}
		// Download the new file
		UpdateDownloader updateDownloader = new UpdateDownloader(updateInformation, temporaryFileLocation);
		try {
			updateDownloader.download();
		} catch (IOException e) {
			logger.warn("Failed to download update file.");
		}
		// Install
		UpdateInstaller updateInstaller = new UpdateInstaller(updateDownloader.getTemporaryFileLocation());
		try {
			updateInstaller.install();
		} catch (IOException e) {
			logger.warn("Failed to overwrite the old plug-in file with the new one!");
		}
		// Remove temporary files
		logger.debug("Deleting temporary file...");
		final File tmp = new File(updateDownloader.getTemporaryFileLocation());
		if (tmp.exists()) {
			tmp.delete();
		}
		logger.info("Done! Please restart your server to finish the update process.");
	}

	private UpdateInformation check(UpdateInformationType updateInformationType) throws IOException, ParseException {
		UpdateChecker updateChecker = new UpdateChecker();
		switch (updateInformationType) {
			case RELEASE:
				return updateChecker.getUpdateInformation();
			case BETA:
				return updateChecker.getUpdateInformation("beta");
			case TEST:
				return updateChecker.getUpdateInformation("test");
			default:
				throw new EnumConstantNotPresentException(UpdateInformationType.class, null);
		}
	}

}

enum UpdateInformationType {

	RELEASE, BETA, TEST

}
