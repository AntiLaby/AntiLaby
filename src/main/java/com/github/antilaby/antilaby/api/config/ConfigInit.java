package com.github.antilaby.antilaby.api.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.antilaby.antilaby.util.Constants;

/**
 * Initialize the configuration file and update it from older versions.
 * 
 * @author NathanNr
 */

public class ConfigInit {

	private File file;
	private FileConfiguration cfg;
	private int oldVersion;
	public static final int CURRENT_CONFIG_VERSION = 3;
	public static final String CONFIG_VERSION_PATH = "AntiLaby.ConfigVersion";

	public ConfigInit(File file, FileConfiguration cfg) throws IOException {
		this.file = file;
		this.cfg = cfg;
		if (cfg.getInt(CONFIG_VERSION_PATH) == 3) {
			addDefaults();
			save();
			return;
		} else {
			update();
		}
	}

	private void addDefaults() {
		// TODO: create new configuration design
		cfg.options().header(
				"AntiLaby plugin by NathanNr, https://www.spigotmc.org/resources/" + Constants.RESOURCE_ID + "/");
		cfg.addDefault("AntiLaby.EnableBypassWithPermission", true);
		// LabyMod 3 features:
		cfg.addDefault("AntiLaby.LabyModPlayerKick.Enable", true);
		cfg.addDefault("AntiLaby.LabyModFeature.SATURATION_BAR", true);
		cfg.addDefault("AntiLaby.LabyModFeature.CHAT", true);
		cfg.addDefault("AntiLaby.LabyModFeature.GUI_ALL", true);
		cfg.addDefault("AntiLaby.LabyModFeature.GUI_POTION_EFFECTS", true);
		cfg.addDefault("AntiLaby.LabyModFeature.GUI_ARMOR_HUD", true);
		cfg.addDefault("AntiLaby.LabyModFeature.GUI_ITEM_HUD", true);
		cfg.addDefault("AntiLaby.LabyModFeature.TAGS", true);
		cfg.addDefault("AntiLaby.LabyModFeature.ANIMATIONS", true);
		cfg.addDefault("AntiLaby.LabyModFeature.BLOCKBUILD", false);
		cfg.addDefault("AntiLaby.LabyModFeature.IMPROVED_LAVA", false);
		cfg.addDefault("AntiLaby.LabyModFeature.CROSSHAIR_SYNC", false);
		cfg.addDefault("AntiLaby.LabyModFeature.REFILL_FIX", false);
		// Old LabyMod features:
		cfg.addDefault("AntiLaby.LabyModFeature.FOOD", false);
		cfg.addDefault("AntiLaby.LabyModFeature.GUI", false);
		cfg.addDefault("AntiLaby.LabyModFeature.NICK", false);
		cfg.addDefault("AntiLaby.LabyModFeature.EXTRAS", false);
		cfg.addDefault("AntiLaby.LabyModFeature.ANIMATIONS", false);
		cfg.addDefault("AntiLaby.LabyModFeature.POTIONS", false);
		cfg.addDefault("AntiLaby.LabyModFeature.ARMOR", false);
		cfg.addDefault("AntiLaby.LabyModFeature.DAMAGEINDICATOR", true);
		cfg.addDefault("AntiLaby.LabyModFeature.MINIMAP_RADAR", true);
		cfg.addDefault("AntiLaby.Update.AutoUpdate.Release", true); // TODO: New auto-updater
		cfg.addDefault("AntiLaby.Update.AutoUpdate.Beta", false);
		final List<String> commands = cfg.getStringList("AntiLaby.LabyModPlayerCommands");
		commands.add("#These commands will be executed once if a player with LabyMod joins the server.");
		commands.add("#If the player has the permission \"antilaby.bypasscommands\" the commands won't be executed.");
		commands.add("#You can use %PLAYER% to get the player's name. Example (remove \"#\" to enable):");
		commands.add("#/tellraw %PLAYER% {\"text\":\"Welcome LabyMod player!\"}");
		if (cfg.getList("AntiLaby.LabyModPlayerCommands") == null)
			cfg.set("AntiLaby.LabyModPlayerCommands", commands);
		cfg.addDefault("AntiLaby.DebugMode", false);
		cfg.addDefault("AntiLaby.ConfigVersion", 3);
		cfg.options().copyDefaults(true);
	}

	private void update() {
		if (cfg.getString("AntiLaby.ConfigVersion") != null)
			oldVersion = cfg.getInt("AntiLaby.ConfigVersion");
		else
			oldVersion = 0;
		if (oldVersion == 1) {
			// TODO: update
			addDefaults();
			try {
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (oldVersion == 2) {
			// TODO: update
			addDefaults();
			try {
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void save() throws IOException {
		cfg.save(file);
	}

}
