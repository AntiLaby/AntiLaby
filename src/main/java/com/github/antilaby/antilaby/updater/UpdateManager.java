package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.config.ConfigReader;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.AntiLaby;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
  private static final String URL = "http://localhost:8080/api/v1/com/github/antilaby/antilaby" +
      "/update"; // TODO Insert final URL

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
    VersionInfo versionInfo;
    try {
      if (!includeTest) {
        if (!includeBeta) {
          versionInfo = check(UpdateChannel.RELEASE);
        } else {
          versionInfo = check(UpdateChannel.BETA);
        }
      } else {
        versionInfo = check(UpdateChannel.TEST);
      }
    } catch (IOException e) {
      logger.warn("Failed to check for updates: Network error");
      return;
    }
    if (versionInfo == null) {
      logger.warn("Failed to check for updates.");
      return;
    }
    // Check if a newer version is available; cancel the update process, if the most recent
    // version is already installed
    if (versionInfo.version.lessThanOrEqualTo(AntiLaby.getVersion())) {
      logger.info("The most recent version is already installed.");
      return;
    }
    // Download the new file
    UpdateDownloader updateDownloader = new UpdateDownloader(versionInfo, tempFile);
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
  private VersionInfo check(UpdateChannel updateChannel) throws IOException {
    return new UpdateChecker(URL).getUpdateInformation(updateChannel.name().toLowerCase());
  }

}
