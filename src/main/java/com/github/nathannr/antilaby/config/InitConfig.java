package com.github.nathannr.antilaby.config;

import java.util.List;

import org.apache.logging.log4j.Level;

import com.github.nathannr.antilaby.main.AntiLaby;
import com.github.nathannr.antilaby.update.VersionType;

import de.heisluft.antilaby.log.Logger;
import de.heisluft.antilaby.util.Constants;

public class InitConfig {
	
	public static final int CURRENT_CONFIG_VERSION = 2;
	
	private static final Logger LOG = new Logger("Config");
	
	public static int getCurrentConfigVersion() {
		return CURRENT_CONFIG_VERSION;
	}

	private final AntiLaby plugin;
	
	private int configVersion;
	
	public InitConfig(AntiLaby plugin) {
		this.plugin = plugin;
	}
	
	public void convertConfig() {
		if (configVersion == 1) {
			// Convert...
			final boolean enableBypassWithPermission = ConfigFile.getCfg()
					.getBoolean("AntiLaby.EnableBypassWithPermission");
			final boolean labyModPlayerKickEnable = ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerKick.Enable");
			final boolean FOOD = ConfigFile.getCfg().getBoolean("AntiLaby.disable.FOOD");
			final boolean GUI = ConfigFile.getCfg().getBoolean("AntiLaby.disable.GUI");
			final boolean NICK = ConfigFile.getCfg().getBoolean("AntiLaby.disable.NICK");
			// boolean BLOCKBUILD =
			// ConfigFile.getCfg().getBoolean("AntiLaby.disable.BLOCKBUILD");
			final boolean CHAT = ConfigFile.getCfg().getBoolean("AntiLaby.disable.CHAT");
			final boolean EXTRAS = ConfigFile.getCfg().getBoolean("AntiLaby.disable.EXTRAS");
			final boolean ANIMATIONS = ConfigFile.getCfg().getBoolean("AntiLaby.disable.ANIMATIONS");
			final boolean POTIONS = ConfigFile.getCfg().getBoolean("AntiLaby.disable.POTIONS");
			final boolean ARMOR = ConfigFile.getCfg().getBoolean("AntiLaby.disable.ARMOR");
			final boolean DAMAGEINDICATOR = ConfigFile.getCfg().getBoolean("AntiLaby.disable.DAMAGEINDICATOR");
			final boolean MINIMAP_RADAR = ConfigFile.getCfg().getBoolean("AntiLaby.disable.MINIMAP_RADAR");
			final List<String> labyModPlayerCommands = ConfigFile.getCfg()
					.getStringList("AntiLaby.LabyModPlayerCommands");
			ConfigFile.resetConfig();
			generateNewConfig();
			ConfigFile.getCfg().set("AntiLaby.EnableBypassWithPermission", enableBypassWithPermission);
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerKick.Enable", labyModPlayerKickEnable);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.FOOD", FOOD);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.SATURATION_BAR", FOOD);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.GUI_ALL", GUI);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.NICK", NICK);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.TAGS", NICK);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.CHAT", CHAT);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.EXTRAS", EXTRAS);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.ANIMATIONS", ANIMATIONS);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.GUI_POTION_EFFECTS", POTIONS);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.POTIONS", POTIONS);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.GUI_ARMOR_HUD", ARMOR);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.ARMOR", ARMOR);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR", DAMAGEINDICATOR);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR", MINIMAP_RADAR);
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerCommands", null);
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerCommands", labyModPlayerCommands);
			LOG.log(Level.INFO, "Your configuration file has been converted from version '" + configVersion
					+ "' to version '" + CURRENT_CONFIG_VERSION + "'.");
			ConfigFile.saveFile();
		} else LOG.log(Level.ERROR, "Failed to convert configuration file from version '" + configVersion
				+ "' to version '" + CURRENT_CONFIG_VERSION + "'!");
	}
	
	private void generateNewConfig() {
		ConfigFile.getCfg().options().header(
				"AntiLaby plugin by NathanNr, https://www.spigotmc.org/resources/" + Constants.RESOURCE_ID + "/");
		ConfigFile.getCfg().addDefault("AntiLaby.EnableBypassWithPermission", false);
		ConfigFile.getCfg().addDefault("AntiLaby.LabyModPlayerKick.Enable", false);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.SATURATION_BAR", true);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.CHAT", true);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.GUI_ALL", true);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.GUI_POTION_EFFECTS", true);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.GUI_ARMOR_HUD", true);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.GUI_ITEM_HUD", true);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.TAGS", true);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Disable.ANIMATIONS", false);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Enable.BLOCKBUILD", false);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Enable.IMPROVED_LAVA", false);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Enable.CROSSHAIR_SYNC", false);
		ConfigFile.getCfg().addDefault("AntiLaby.Features.Enable.REFILL_FIX", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.FOOD", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.GUI", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.NICK", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.EXTRAS", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.ANIMATIONS", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.POTIONS", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.ARMOR", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR", true);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR", true);
		final List<String> labyModPlayerCommands = ConfigFile.getCfg().getStringList("AntiLaby.LabyModPlayerCommands");
		labyModPlayerCommands.add("#These commands will be executed once if a player with LabyMod joins the server.");
		labyModPlayerCommands
				.add("#If the player has the permission \"antilaby.bypasscommands\" the commands won't be executed.");
		labyModPlayerCommands.add("#You can use %PLAYER% to get the player's name. Example (remove \"#\" to enable):");
		labyModPlayerCommands.add("#/tellraw %PLAYER% {\"text\":\"Welcome LabyMod player!\"}");
		if (ConfigFile.getCfg().getList("AntiLaby.LabyModPlayerCommands") == null)
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerCommands", labyModPlayerCommands);
		if (plugin.getVersionType().equals(VersionType.RELEASE))
			ConfigFile.getCfg().addDefault("AntiLaby.Update.AutoUpdate", true);
		else ConfigFile.getCfg().set("AntiLaby.Update.AutoUpdate",
				"Auto-update is not available in " + plugin.getVersionType().toString().toLowerCase()
						+ " versions! You can update manually: https://www.spigotmc.org/resources/"
						+ Constants.RESOURCE_ID + "/");
		ConfigFile.getCfg().addDefault("AntiLaby.ConfigVersion", 2);
		ConfigFile.getCfg().options().copyDefaults(true);
		ConfigFile.saveFile();
		if (!ConfigFile.getCfg().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("true"))
			if (!ConfigFile.getCfg().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("false"))
				if (plugin.getVersionType().equals(VersionType.RELEASE)) {
				ConfigFile.getCfg().set("AntiLaby.Update.AutoUpdate", true);
				ConfigFile.saveFile();
				}
	}
	
	public int getConfigVersion() {
		return configVersion;
	}
	
	public void init() {
		// Init config
		ConfigFile.init();
		if (ConfigFile.getCfg().getString("AntiLaby.ConfigVersion") == null) {
			// No config or old config
			if (plugin.getConfig().getString("AntiLaby.disable.FOOD") == null) {
				// No config
				configVersion = 0;
				generateNewConfig();
			} else {
				// Old config, convert
				configVersion = 1;
				convertConfig();
			}
		} else if (ConfigFile.getCfg().getInt("AntiLaby.ConfigVersion") == 2) {
			// New config
			configVersion = 2;
			generateNewConfig();
		} else // Unknown config, reset config
			resetConfig();
		if (ConfigFile.getCfg().getInt("AntiLaby.ConfigVersion") > 2
				|| ConfigFile.getCfg().getInt("AntiLaby.ConfigVersion") < 1)
			resetConfig();
	}
	
	private void resetConfig() {
		generateNewConfig();
		generateNewConfig();
	}
	
}
