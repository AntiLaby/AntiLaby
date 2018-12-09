package com.github.antilaby.antilaby.api.updater;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.antilaby.antilaby.log.Logger;

/**
 * Checks which updates are available
 * 
 * @author NathanNr
 */

public class UpdateChecker {

  private final Logger LOG = new Logger("UpdateChecker");

  private JavaPlugin plugin;
  private URL url;
  private long latestUpdateCheck;

  private ArrayList<Update> availableUpdates;
  private int availableUpdatesCount;

  /**
   * @param plugin
   *            The JavaPlugin
   */
  public UpdateChecker(JavaPlugin plugin) {
    this.plugin = plugin;
    try {
      // TODO Add the default URL.
      this.url = new URL("");
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param plugin
   *            The JavaPlugin
   * @param url
   *            The URL which provides the update information
   */
  public UpdateChecker(JavaPlugin plugin, URL url) {
    this.plugin = plugin;
    this.url = url;
  }

  public void check() {
    LOG.debug("Checking for updates...");
    availableUpdates = new ArrayList<>();
    latestUpdateCheck = System.currentTimeMillis();
    // TODO: Check for updates and create Update objects, save them into
    // availableUpdates
    LOG.debug("Found " + availableUpdatesCount + " available update versions.");
  }

  /**
   * @return The JavaPlugin
   */
  public JavaPlugin getPlugin() {
    return plugin;
  }

  /**
   * @return The URL which provides the update information
   */
  public URL getUrl() {
    return url;
  }

  /**
   * @return The time of the latest update check
   */
  public long getLatestUpdateCheck() {
    return this.latestUpdateCheck;
  }

  /**
   * @return All available updates
   */
  public List<Update> getAvailableUpdates() {
    return availableUpdates;
  }

  /**
   * @return The count of all available updates
   */
  public int getAvailableUpdatesCount() {
    return availableUpdatesCount;
  }

}
