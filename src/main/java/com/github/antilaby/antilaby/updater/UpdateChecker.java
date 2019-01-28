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
   * The location of the online update information provider TODO: Add the final URL, add alternate URL
   */
  private String url;

  private UpdateInformation updateInformation;

  public UpdateChecker(String uri) {
    this.url = uri;
  }

  public UpdateInformation getUpdateInformation() throws IOException {
    return getUpdateInformation("release");
  }

  public UpdateInformation getUpdateInformation(String type) throws IOException {
    if (updateInformation != null) {
      return updateInformation;
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
    JSONObject jsonObject = (JSONObject) JSONValue.parse(raw);
    updateInformation = new UpdateInformation(
        (String) jsonObject.get("version"),
        Math.toIntExact((long) jsonObject.get("versionId")),
        (String) jsonObject.get("downloadUrl"),
        (String) jsonObject.get("versionType"),
        Math.toIntExact((long) jsonObject.get("updatePriority")),
        (String) jsonObject.get("sha256"),
        (String) jsonObject.get("updateNote")
    );
    logger.debug("Done!");

    return updateInformation;
  }

  public void forceReload() {
    updateInformation = null;
    logger.debug("The reload of the data at the next request has been forced.");
  }
}
