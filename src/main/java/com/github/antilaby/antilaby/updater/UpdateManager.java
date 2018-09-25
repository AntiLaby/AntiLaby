package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.api.config.ConfigReader;
import com.github.antilaby.antilaby.api.exceptions.InternalException;
import com.github.antilaby.antilaby.api.updater.Update;
import com.github.antilaby.antilaby.log.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Manage the auto-update process
 *
 * @author NathanNr
 */
public class UpdateManager {

	private Logger logger = new Logger("AutoUpdateManager");

	private JavaPlugin javaPlugin;

	private ConfigReader configReader;

	private boolean autoUpdate;
	private boolean includeBeta;

	public UpdateManager(JavaPlugin javaPlugin) {
		this.javaPlugin = javaPlugin;
		// Get update information from the configuration file
		this.configReader = new ConfigReader();
		this.autoUpdate = configReader.getAutoUpdate().autoUpdateReleaseEnabled();
		this.includeBeta = configReader.getAutoUpdate().autoUpdateBetaEnabled();
	}

	public boolean run() {
		// Checking for updates
		if (autoUpdate == false) {
			logger.info("Auto-update has been disabled in the configuration file.");
			return true;
		}
		logger.debug("Checking for new updates...");
		UpdateInformation updateInformation;
		try {
			if (!includeBeta) {
				updateInformation = check(UpdateInformationType.RELEASE);
			} else {
				updateInformation = check(UpdateInformationType.BETA);
			}
		} catch (IOException e) {
			logger.info("Failed to check for updates: Network error");
			return false;
		} catch (ParseException e) {
			logger.info("Failed to check for updates: Parsing error");
			return false;
		}
		if (updateInformation == null) {
			logger.info("Failed to check for updates.");
			return false;
		}
		// TODO
		return false;
	}

	private UpdateInformation check(UpdateInformationType updateInformationType) throws IOException, ParseException {
		// TODO: new thread
		UpdateChecker updateChecker = new UpdateChecker();
		switch (updateInformationType) {
			case RELEASE:
				return updateChecker.getUpdateInformation();
			case BETA:
				return updateChecker.getUpdateInformation("beta");
			default:
				throw new InternalException("UpdateManager", "Unknown UpdateInformationType", null);
		}
	}

}

enum UpdateInformationType {

	RELEASE, BETA

}