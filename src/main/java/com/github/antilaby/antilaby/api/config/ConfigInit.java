package com.github.antilaby.antilaby.api.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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

	public ConfigInit(File file, FileConfiguration cfg) throws IOException {
		this.file = file;
		this.cfg = cfg;
		if(cfg.getInt(CONFIG_VERSION_PATH) == Constants.CURRENT_CONFIG_VERSION) {
			addDefaults();
			save();
		} else update();
	}

	/**
	 * Add the default values to the configuration file.
	 */
	private void addDefaults() {
		// TODO: create new configuration design
		cfg.options().header(
				"AntiLaby plugin by NathanNr, https://www.spigotmc.org/resources/" + Constants.RESOURCE_ID + "/");
		cfg.options().header(
				"AntiLaby plugin by NathanNr, https://www.spigotmc.org/resources/" + Constants.RESOURCE_ID + "/");
		cfg.addDefault("AntiLaby.EnableBypassWithPermission", true);
		// TODO collect all LabyMod feature settings in a String list
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
		if(cfg.getList("AntiLaby.LabyModPlayerCommands") == null) cfg.set("AntiLaby.LabyModPlayerCommands", commands);
		cfg.addDefault("AntiLaby.DebugMode", false);
		cfg.addDefault("AntiLaby.ConfigVersion", 3);
		cfg.options().copyDefaults(true);
	}

	/**
	 * Updates the configuration file from an older version to the latest configuration version.
	 */
	@SuppressWarnings("unused")
	private void update() {
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

	/**
	 * Saves the configuration file.
	 *
	 * @throws IOException
	 * 		If the configuration somehow failed to save
	 */
	private void save() throws IOException {
		cfg.save(file);
	}

	/**
	 * Delete the configuration file. This method is used after updating the configuration file from an older
	 * version.
	 */
	private void delete() {
		file.delete();
		file = new File(AntiLaby.getInstance().getDataFolder() + "/config.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
	}

}
