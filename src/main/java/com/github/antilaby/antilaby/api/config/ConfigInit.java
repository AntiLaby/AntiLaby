package com.github.antilaby.antilaby.api.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.api.exceptions.InternalException;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;

/**
 * Initialize the configuration file and update it from older versions.
 *
 * @author NathanNr
 */
public class ConfigInit {

	private File file;
	private FileConfiguration cfg;
	private static final String CONFIG_VERSION_PATH = "AntiLaby.ConfigVersion";

	/**
	 * Initialize the configuration file and update it from older versions.
	 *
	 * @param file the configuration file
	 * @param cfg the file configuration instance of the configuration file
	 * @throws IOException
	 */
	public ConfigInit(File file, FileConfiguration cfg) throws IOException {
		this.file = file;
		this.cfg = cfg;

		// Check if the configuration file have to be updated
		if(cfg.get(CONFIG_VERSION_PATH) == null) {
			addDefaults();
			save();
		} else if(cfg.getInt(CONFIG_VERSION_PATH) == Constants.CURRENT_CONFIG_VERSION) {
			addDefaults();
			save();
		} else {
			update();
		}
	}

	/**
	 * Add the default values to the configuration file.
	 */
	private void addDefaults() {
		cfg.options().header(
				"AntiLaby plugin by NathanNr and heisluft, " + Constants.RESOURCE_LINK);
		cfg.addDefault("AntiLaby.EnableBypassWithPermission", true);
		cfg.addDefault("AntiLaby.LabyModPlayerAction.Kick.Enable", false);
		cfg.addDefault("AntiLaby.LabyModPlayerAction.Ban.Enable", false); // TODO: implement LabyModPlayerBan

		// Additional plug-in channels. Clients who use them, will be blocked.
		// TODO: Rework the config reader and block clients which open these plug-in channels.
		if(cfg.getStringList("AntiLaby.AdditionalPluginChannels") == null) {
			ArrayList<String> additionalPluginChannels = new ArrayList<>();
			additionalPluginChannels.add("#Here you can add additional plug-in channels which will be blocked.");
			cfg.set("AntiLaby.AdditionalPluginChannels", additionalPluginChannels);
		}

		// Add default values for auto-updates
		cfg.addDefault("AntiLaby.Update.AutoUpdate.Release", true); // TODO: New auto-updater
		cfg.addDefault("AntiLaby.Update.AutoUpdate.Beta", false);

		// Add default values for the feature handling of LabyMod
		final List<String> disabledFeatures = cfg.getStringList("AntiLaby.LabyModFeatures.Disable");
		final List<String> enabledFeatures = cfg.getStringList("AntiLaby.LabyModFeatures.Enable");
		for(LabyModFeature labyModFeature : LabyModFeature.values()) {
			switch(labyModFeature.getDefaultValue()) {
				case "enabled":
					disabledFeatures.add(labyModFeature.toString());
					break;
				case "disabled":
					enabledFeatures.add(labyModFeature.toString());
					break;
				default:
					throw new InternalException("Configuration", "Detected unknown default LabyMod feature value while creating the configuration file.", null);
			}
		}

		// Set the string lists to the configuration file, if they do not exist yet
		if(cfg.getList("AntiLaby.LabyModFeatures.Disable") == null)
			cfg.set("AntiLaby.LabyModFeatures.Disable", disabledFeatures);
		if(cfg.getList("AntiLaby.LabyModFeatures.Enable") == null)
			cfg.set("AntiLaby.LabyModFeatures.Enable", enabledFeatures);
		if(cfg.getList("AntiLaby.LabyModPlayerCommands") == null) {
			final List<String> commands = cfg.getStringList("AntiLaby.LabyModPlayerCommands");
			commands.add("#These commands will be executed once if a player with LabyMod joins the server.");
			commands.add(
					"#If the player has the permission \"antilaby.bypasscommands\" the commands won't be executed.");
			commands.add("#You can use %PLAYER% to get the player's name. Example (remove \"#\" to enable):");
			commands.add("#/tellraw %PLAYER% {\"text\":\"Welcome LabyMod player!\"}");
			cfg.set("AntiLaby.LabyModPlayerCommands", commands);
		}
		cfg.addDefault("AntiLaby.DebugMode", false);
		cfg.addDefault("AntiLaby.ConfigVersion", Constants.CURRENT_CONFIG_VERSION);
		cfg.options().copyDefaults(true);
	}

	/**
	 * Updates the configuration file from an older version to the latest configuration version.
	 */
	@SuppressWarnings("unused")
	private void update() {
		// The old file will be overwritten currently.
		// TODO: Update the configuration updater.
		if(file.exists()) {
			file.delete();
			cfg = YamlConfiguration.loadConfiguration(file);
			addDefaults();
		}
		if(false) { // Currently not in use
			int oldVersion;
			if(cfg.getString("AntiLaby.ConfigVersion") != null) oldVersion = cfg.getInt("AntiLaby.ConfigVersion");
			else oldVersion = 0;
			if(oldVersion == 1) {
				final boolean bypassPermissionEnabled = cfg.getBoolean("AntiLaby.EnableBypassWithPermission");
				final boolean labyKick = cfg.getBoolean("AntiLaby.LabyModPlayerKick.Enable");
				final boolean food = cfg.getBoolean("AntiLaby.disable.FOOD");
				final boolean gui = cfg.getBoolean("AntiLaby.disable.GUI");
				final boolean nick = cfg.getBoolean("AntiLaby.disable.NICK");
				final boolean chat = cfg.getBoolean("AntiLaby.disable.CHAT");
				final boolean extras = cfg.getBoolean("AntiLaby.disable.EXTRAS");
				final boolean animations = cfg.getBoolean("AntiLaby.disable.ANIMATIONS");
				final boolean potions = cfg.getBoolean("AntiLaby.disable.POTIONS");
				final boolean armor = cfg.getBoolean("AntiLaby.disable.ARMOR");
				final boolean damageIndicator = cfg.getBoolean("AntiLaby.disable.DAMAGEINDICATOR");
				final boolean minimapRadar = cfg.getBoolean("AntiLaby.disable.MINIMAP_RADAR");
				final List<String> commands = cfg.getStringList("AntiLaby.LabyModPlayerCommands");
				delete();
				addDefaults();
				// TODO Delete the old file and create the new one with the stored values.
				try {
					save();
				} catch(IOException e) {
					e.printStackTrace();
				}
			} else if(oldVersion == 2) {
				boolean bypassWithPermission = cfg.getBoolean("AntiLaby.EnableBypassWithPermission");
				boolean labyModPlayerKickEnabled = cfg.getBoolean("AntiLaby.LabyModPlayerKick.Enable");
				// LabyMod 3 features:
				boolean saturationBar = cfg.getBoolean("AntiLaby.Features.Disable.SATURATION_BAR");
				boolean chat = cfg.getBoolean("AntiLaby.Features.Disable.CHAT");
				boolean guiAll = cfg.getBoolean("AntiLaby.Features.Disable.GUI_ALL");
				boolean guiPotion = cfg.getBoolean("AntiLaby.Features.Disable.GUI_POTION_EFFECTS");
				boolean guiArmor = cfg.getBoolean("AntiLaby.Features.Disable.GUI_ARMOR_HUD");
				boolean guiItem = cfg.getBoolean("AntiLaby.Features.Disable.GUI_ITEM_HUD");
				boolean tags = cfg.getBoolean("AntiLaby.Features.Disable.TAGS");
				boolean animations = cfg.getBoolean("AntiLaby.Features.Disable.ANIMATIONS");
				boolean blockbuild = cfg.getBoolean("AntiLaby.Features.Enable.BLOCKBUILD");
				boolean improvedLava = cfg.getBoolean("AntiLaby.Features.Enable.IMPROVED_LAVA");
				boolean crosshairSync = cfg.getBoolean("AntiLaby.Features.Enable.CROSSHAIR_SYNC");
				boolean refillFix = cfg.getBoolean("AntiLaby.Features.Enable.REFILL_FIX");
				// Features from earlier versions of LabyMod:
				boolean food = cfg.getBoolean("AntiLaby.OldFeatures.Disable.FOOD");
				boolean gui = cfg.getBoolean("AntiLaby.OldFeatures.Disable.GUI");
				boolean nick = cfg.getBoolean("AntiLaby.OldFeatures.Disable.NICK");
				boolean extras = cfg.getBoolean("AntiLaby.OldFeatures.Disable.EXTRAS");
				boolean potions = cfg.getBoolean("AntiLaby.OldFeatures.Disable.POTIONS");
				boolean armor = cfg.getBoolean("AntiLaby.OldFeatures.Disable.ARMOR");
				boolean damageIndicator = cfg.getBoolean("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR");
				boolean minimapRadar = cfg.getBoolean("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR");
				List<String> commands = cfg.getStringList("AntiLaby.LabyModPlayerCommands");
				boolean autoUpdate = cfg.getBoolean("AntiLaby.Update.AutoUpdate");
				boolean debugMode = cfg.getBoolean("AntiLaby.DebugMode");
				delete();
				addDefaults();
				// TODO Delete the old file and create the new one with the stored values.
				try {
					save();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Save the configuration file.
	 *
	 * @throws IOException
	 * 		If the configuration somehow failed to save
	 */
	private void save() throws IOException {
		cfg.save(file);
	}

	/**
	 * Delete the configuration file. This method is used after updating the configuration file from an older version.
	 */
	private void delete() {
		file.delete();
		file = new File(AntiLaby.getInstance().getDataFolder() + "/config.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
	}

}
