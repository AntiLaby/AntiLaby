package com.github.antilaby.antilaby.config;

import com.github.antilaby.antilaby.api.updater.VersionType;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;

import java.util.List;

/**
 * Initialize the configuration file and update it from older versions. This class will be removed in the future if the
 * new configuration system is ready to use!
 *
 * @author NathanNr
 */
public class InitConfig {

	private static final Logger LOG = new Logger("Config");
	public static final int CURRENT_CONFIG_VERSION = 2;

	public static int getCurrentConfigVersion() {
		return CURRENT_CONFIG_VERSION;
	}

	private final AntiLaby plugin;

	private int configVersion;

	public InitConfig(AntiLaby plugin) {
		this.plugin = plugin;
	}

	public int getConfigVersion() {
		return configVersion;
	}

	public void init() {
		// Init config
		ConfigFile.init();
		if(ConfigFile.getCfg().getString("AntiLaby.ConfigVersion") == null) {
			// No config or old config
			if(plugin.getConfig().getString("AntiLaby.disable.FOOD") == null) {
				// No config
				configVersion = 0;
				generateNewConfig();
			} else {
				// Old config, boolToInt
				configVersion = 1;
				convertConfig();
			}
		} else if(ConfigFile.getCfg().getInt("AntiLaby.ConfigVersion") == 2) {
			// New config
			configVersion = 2;
			generateNewConfig();
		} else // Unknown config, reset config
			resetConfig();
		if(ConfigFile.getCfg().getInt("AntiLaby.ConfigVersion") > 2 || ConfigFile.getCfg().getInt(
				"AntiLaby.ConfigVersion") < 1) resetConfig();
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
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.POTIONS", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.ARMOR", false);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR", true);
		ConfigFile.getCfg().addDefault("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR", true);
		final List<String> commands = ConfigFile.getCfg().getStringList("AntiLaby.LabyModPlayerCommands");
		commands.add("#These commands will be executed once if a player with LabyMod joins the server.");
		commands.add("#If the player has the permission \"antilaby.bypasscommands\" the commands won't be executed.");
		commands.add("#You can use %PLAYER% to get the player's name. Example (remove \"#\" to enable):");
		commands.add("#/tellraw %PLAYER% {\"text\":\"Welcome LabyMod player!\"}");
		if(ConfigFile.getCfg().getList("AntiLaby.LabyModPlayerCommands") == null)
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerCommands", commands);
		if(plugin.getVersionType().equals(VersionType.RELEASE))
			ConfigFile.getCfg().addDefault("AntiLaby.Update.AutoUpdate", true);
		else ConfigFile.getCfg().set("AntiLaby.Update.AutoUpdate",
				"Auto-update is not available in " + plugin.getVersionType().toString().toLowerCase() + " versions! "
						+ "You can update manually: https://www.spigotmc.org/resources/" + Constants.RESOURCE_ID +
						'/');
		ConfigFile.getCfg().addDefault("AntiLaby.ConfigVersion", 2);
		ConfigFile.getCfg().addDefault("AntiLaby.DebugMode", false);
		ConfigFile.getCfg().options().copyDefaults(true);
		ConfigFile.saveFile();
		if(!ConfigFile.getCfg().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("true"))
			if(!ConfigFile.getCfg().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("false"))
				if(plugin.getVersionType().equals(VersionType.RELEASE)) {
					ConfigFile.getCfg().set("AntiLaby.Update.AutoUpdate", true);
					ConfigFile.saveFile();
				}
	}

	public void convertConfig() {
		if(configVersion == 1) {
			// Convert...
			final boolean bypassPermissionEnabled = ConfigFile.getCfg().getBoolean(
					"AntiLaby.EnableBypassWithPermission");
			final boolean labyKick = ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerKick.Enable");
			final boolean food = ConfigFile.getCfg().getBoolean("AntiLaby.disable.FOOD");
			final boolean gui = ConfigFile.getCfg().getBoolean("AntiLaby.disable.GUI");
			final boolean nick = ConfigFile.getCfg().getBoolean("AntiLaby.disable.NICK");
			// boolean BLOCKBUILD =
			// ConfigFile.getCfg().getBoolean("AntiLaby.disable.BLOCKBUILD");
			final boolean chat = ConfigFile.getCfg().getBoolean("AntiLaby.disable.CHAT");
			final boolean extras = ConfigFile.getCfg().getBoolean("AntiLaby.disable.EXTRAS");
			final boolean animations = ConfigFile.getCfg().getBoolean("AntiLaby.disable.ANIMATIONS");
			final boolean potions = ConfigFile.getCfg().getBoolean("AntiLaby.disable.POTIONS");
			final boolean armor = ConfigFile.getCfg().getBoolean("AntiLaby.disable.ARMOR");
			final boolean damageIndicator = ConfigFile.getCfg().getBoolean("AntiLaby.disable.DAMAGEINDICATOR");
			final boolean minimapRadar = ConfigFile.getCfg().getBoolean("AntiLaby.disable.MINIMAP_RADAR");
			final List<String> commands = ConfigFile.getCfg().getStringList("AntiLaby.LabyModPlayerCommands");
			ConfigFile.resetConfig();
			generateNewConfig();
			ConfigFile.getCfg().set("AntiLaby.EnableBypassWithPermission", bypassPermissionEnabled);
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerKick.Enable", labyKick);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.FOOD", food);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.SATURATION_BAR", food);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.GUI_ALL", gui);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.NICK", nick);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.TAGS", nick);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.CHAT", chat);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.EXTRAS", extras);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.ANIMATIONS", animations);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.GUI_POTION_EFFECTS", potions);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.POTIONS", potions);
			ConfigFile.getCfg().set("AntiLaby.Features.Disable.GUI_ARMOR_HUD", armor);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.ARMOR", armor);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR", damageIndicator);
			ConfigFile.getCfg().set("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR", minimapRadar);
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerCommands", null);
			ConfigFile.getCfg().set("AntiLaby.LabyModPlayerCommands", commands);
			LOG.info(
					"Your configuration file has been converted from version '" + configVersion + "' to version '" +
							CURRENT_CONFIG_VERSION + "'.");
			ConfigFile.saveFile();
		} else LOG.error(
				"Failed to boolToInt configuration file from version '" + configVersion + "' to version '" +
						CURRENT_CONFIG_VERSION + "'!");
	}

	private void resetConfig() {
		generateNewConfig();
		generateNewConfig();
	}

}
