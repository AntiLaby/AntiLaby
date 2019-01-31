package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Checks for AntiLaby updates. This class loads, caches and parses the update information.
 *
 * @author NathanNr
 */
public class UpdateChecker {

  private static final Logger logger = new Logger("UpdateChecker");

  /**
   * The location of the online update information provider.
   * TODO: Add the final URL, add alternate URL
   */
  private String url;

  private VersionInfo versionInfo;

  UpdateChecker(String uri) {
    this.url = uri;
  }

  public VersionInfo getVersionInfo() throws IOException {
    return getUpdateInformation("release");
  }

  VersionInfo getUpdateInformation(String type) throws IOException {
    if (versionInfo != null) {
      return versionInfo;
    }
    // Read from REST server
    logger.debug("Reading data from the REST server...");
    String raw;
    try (Scanner sc = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8.toString())) {
      sc.useDelimiter("\\A");
      raw = sc.hasNext() ? sc.next() : "";
    }
    // JSON -> UpdateInformation
    logger.debug("Parsing raw data...");
    versionInfo = VersionInfo.of((JSONObject) JSONValue.parse(raw));
    logger.debug("Done!");

    return versionInfo;
  }

  public void forceReload() {
    versionInfo = null;
    logger.debug("The reload of the data at the next request has been forced.");
  }
}
