package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.config.ConfigReader;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

enum UpdateChannel {

  RELEASE, BETA, TEST

}

/**
 * Manage the auto-update process
 *
 * @author NathanNr
 */
public class UpdateManager extends Thread {

  private static final Logger logger = new Logger("UpdateManager");
  private final Path tempFile = AntiLaby.getInstance().getDataPath().resolve("tmp/AntiLaby.tmp");
  private boolean autoUpdate;
  private boolean includeBeta;
  private boolean includeTest;
  private String url = "http://localhost:8080/api/v1/com/github/antilaby/antilaby/update"; // TODO Insert final url

  public UpdateManager() {
    // Get update information from the configuration file
    ConfigReader configReader = new ConfigReader();
    this.autoUpdate = configReader.getAutoUpdate().release();
    this.includeBeta = configReader.getAutoUpdate().beta();
    this.includeTest = configReader.getAutoUpdate().test();
  }

  @Override
  public void run() {
    // Checking for updates
    if (!autoUpdate) {
      logger.info("Auto-update has been disabled in the configuration file.");
      return;
    }
    logger.debug("Checking for new updates...");
    UpdateInformation updateInformation;
    try {
      if (!includeTest) {
        if (!includeBeta) {
          updateInformation = check(UpdateChannel.RELEASE);
        } else {
          updateInformation = check(UpdateChannel.BETA);
        }
      } else {
        updateInformation = check(UpdateChannel.TEST);
      }
    } catch (IOException e) {
      logger.warn("Failed to check for updates: Network error");
      return;
    }
    if (updateInformation == null) {
      logger.warn("Failed to check for updates.");
      return;
    }
    // Check if a newer version is available; cancel the update process, if the most recent version is already installed
    if (updateInformation.versionId <= Constants.VERSION_ID) {
      logger.info("The most recent version is already installed.");
      return;
    }
    // Download the new file
    UpdateDownloader updateDownloader = new UpdateDownloader(updateInformation, tempFile);
    try {
      updateDownloader.download();
    } catch (IOException e) {
      logger.warn("Failed to download update file.");
    }
    // Install
    try {
      Files.copy(tempFile, AntiLaby.getInstance().getPath());
    } catch (IOException e) {
      logger.warn("Failed to overwrite the old plug-in file with the new one!");
    }
    // Remove temporary file
    logger.debug("Removing temporary file...");
    try {
      Files.delete(tempFile);
    } catch (IOException e) {
      logger.warn("Could not delete temporary file");
    }
    logger.info("Done! Please restart your server to finish the update process.");
  }

  /**
   * Check for a new version of AntiLaby.
   *
   * @param updateChannel the channel to read from
   * @return the update information read
   * @throws IOException if the information could  not be retrieved
   */
  private UpdateInformation check(UpdateChannel updateChannel) throws IOException {
    return new UpdateChecker(url).getUpdateInformation(updateChannel.name().toLowerCase());
  }

}
