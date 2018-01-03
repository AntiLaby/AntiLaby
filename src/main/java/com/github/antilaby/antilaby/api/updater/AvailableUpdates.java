package com.github.antilaby.antilaby.api.updater;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.antilaby.antilaby.log.Logger;

public class AvailableUpdates extends Thread {

	private JavaPlugin plugin;
	private URL url;
	private final Logger LOG = new Logger("UpdateInformation");

	private int availableUpdates;

	public AvailableUpdates(JavaPlugin plugin) {
		this.plugin = plugin;
		try {
			// This is possibly not the final URL
			this.url = new URL("https://github.com/AntiLaby/WebApi/blob/master/AntiLaby/Spigot/update.json");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public AvailableUpdates(JavaPlugin plugin, URL url) {
		this.plugin = plugin;
		this.url = url;
	}

	public void check() {
		LOG.debug("Checking for updates...");

		LOG.debug("Found " + availableUpdates + " available update versions.");
	}

	public List<Update> getUpdates() {
		return new ArrayList<Update>();
	}

}
