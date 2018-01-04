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
	private ArrayList<Update> availableUpdates;
	private long latestUpdateCheck;

	private int availableUpdatesCount;

	public UpdateChecker(JavaPlugin plugin) {
		this.plugin = plugin;
		try {
			// This is possibly not the final URL
			this.url = new URL("https://github.com/AntiLaby/WebApi/blob/master/AntiLaby/Spigot/update.json");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public UpdateChecker(JavaPlugin plugin, URL url) {
		this.plugin = plugin;
		this.url = url;
	}

	public void check() {
		LOG.debug("Checking for updates...");
		availableUpdates = new ArrayList<>();
		latestUpdateCheck = System.currentTimeMillis();
		// TODO: Check for updates and create Update objects
		LOG.debug("Found " + availableUpdatesCount + " available update versions.");
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public URL getUrl() {
		return url;
	}

	public long getLatestUpdateCheck() {
		return this.latestUpdateCheck;
	}

	public List<Update> getAvailableUpdates() {
		return availableUpdates;
	}

}
