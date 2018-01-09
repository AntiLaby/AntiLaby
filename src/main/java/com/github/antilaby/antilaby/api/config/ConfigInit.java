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
		cfg.addDefault("AntiLaby.LabyModPlayerKick.Enable", true);
		cfg.addDefault("AntiLaby.Feature.SATURATION_BAR", true);
		cfg.addDefault("AntiLaby.Feature.CHAT", true);
		cfg.addDefault("AntiLaby.Feature.GUI_ALL", true);
		cfg.addDefault("AntiLaby.Feature.GUI_POTION_EFFECTS", true);
		cfg.addDefault("AntiLaby.Feature.GUI_ARMOR_HUD", true);
		cfg.addDefault("AntiLaby.Feature.GUI_ITEM_HUD", true);
		cfg.addDefault("AntiLaby.Feature.TAGS", true);
		cfg.addDefault("AntiLaby.Feature.ANIMATIONS", true);
		cfg.addDefault("AntiLaby.Feature.BLOCKBUILD", false);
		cfg.addDefault("AntiLaby.Feature.IMPROVED_LAVA", false);
		cfg.addDefault("AntiLaby.Feature.CROSSHAIR_SYNC", false);
		cfg.addDefault("AntiLaby.Feature.REFILL_FIX", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.FOOD", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.GUI", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.NICK", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.EXTRAS", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.ANIMATIONS", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.POTIONS", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.ARMOR", false);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR", true);
		cfg.addDefault("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR", true);
		cfg.addDefault("AntiLaby.Update.AutoUpdate", true); // TODO: New auto-updater
		final List<String> commands = cfg.getStringList("AntiLaby.LabyModPlayerCommands");
		commands.add("#These commands will be executed once if a player with LabyMod joins the server.");
		commands.add("#If the player has the permission \"antilaby.bypasscommands\" the commands won't be executed.");
		commands.add("#You can use %PLAYER% to get the player's name. Example (remove \"#\" to enable):");
		commands.add("#/tellraw %PLAYER% {\"text\":\"Welcome LabyMod player!\"}");
		if (cfg.getList("AntiLaby.LabyModPlayerCommands") == null)
			cfg.set("AntiLaby.LabyModPlayerCommands", commands);
		cfg.addDefault("AntiLaby.ConfigVersion", 3);
		cfg.addDefault("AntiLaby.DebugMode", false);
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
