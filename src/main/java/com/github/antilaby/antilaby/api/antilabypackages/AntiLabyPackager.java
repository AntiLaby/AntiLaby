package com.github.antilaby.antilaby.api.antilabypackages;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.api.config.ConfigReader;
import com.github.antilaby.antilaby.config.ConfigFile;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import com.github.antilaby.antilaby.util.NmsUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Create and send AntiLaby packages to the clients. This file will be reworked
 * in the future!
 *
 * @author NathanNr
 */
public class AntiLabyPackager {

	private final Logger logger = new Logger("AntiLabyPackager");

	private final Player player;
	private Map<LabyModFeature, Boolean> disabledLabyModFeatures;
	private boolean forceIgnoreBypassPermission = false;
	private ConfigReader configReader = new ConfigReader();

	public AntiLabyPackager(Player player) {
		this.player = player;

		this.disabledLabyModFeatures = getConfigLabyModSettings(configReader.getDisabledFeatures(), configReader.getEnabledFeatures());
	}

	public AntiLabyPackager(Player player, boolean forceIgnoreBypassPermission) {
		this.player = player;
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;

		this.disabledLabyModFeatures = getConfigLabyModSettings(configReader.getDisabledFeatures(), configReader.getEnabledFeatures());

	}

	public AntiLabyPackager(Player player, Map<LabyModFeature, Boolean> disabledLabyModFeatures) {
		this.player = player;
		this.disabledLabyModFeatures = disabledLabyModFeatures;
	}

	public AntiLabyPackager(Player player, Map<LabyModFeature, Boolean> disabledLabyModFeatures,
	                        boolean forceIgnoreBypassPermission) {
		this.player = player;
		this.disabledLabyModFeatures = disabledLabyModFeatures;
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
	}

	/**
	 * Map the AntiLaby packages from the configuration file
	 *
	 * @return AntiLaby packages from the configuration file
	 */
	// TODO use String instead of LabyModFeature to allow every String as a feature
	public Map<LabyModFeature, Boolean> getConfigLabyModSettings(List<String> disabledFeatures, List<String> enabledFeatures) {
		Map<LabyModFeature, Boolean> disabledLabyModFeaturesConfig = new EnumMap<>(LabyModFeature.class);
		for (String disabledFeature : disabledFeatures) {
			try {
				disabledLabyModFeaturesConfig.put(LabyModFeature.valueOf(disabledFeature), false);
			} catch (final IllegalArgumentException e) {
				logger.error("ConfigParser: Disabled LabyMod feature '" + disabledFeature + "' is no valid LabyMod feature and will be ignored.");
			}
		}
		for (String enabledFeature : enabledFeatures) {
			try {
				disabledLabyModFeaturesConfig.put(LabyModFeature.valueOf(enabledFeature), true);
			} catch (final IllegalArgumentException e) {
				logger.error("ConfigParser: Enabled LabyMod feature '" + enabledFeature + "' is no valid LabyMod feature and will be ignored.");
			}
		}
		return disabledLabyModFeaturesConfig;
	}

	/**
	 * Send the AntiLaby packages to the player.
	 *
	 * @return success
	 */
	public boolean sendPackages() {
		boolean ignorePlayer = false;
		if (!forceIgnoreBypassPermission) // Ignore players with the bypass permission if enabled in the
			// configuration file.
			if (player.hasPermission(Constants.PERMISSION_BYPASS))
				if (configReader.getEnableBypassWithPermission()) {
					disabledLabyModFeatures = allowEverything();
					ignorePlayer = true;
					AntiLaby.LOG.debug("Player " + player.getName() + " (" + player.getUniqueId()
							+ ") has the permission 'antilaby.bypass' and has been ignored.");
				}
		if (ignorePlayer)
			disabledLabyModFeatures = allowEverything();
		else if (!disabledLabyModFeatures.isEmpty())
			try {
				NmsUtils.setLabyModFeature(player, disabledLabyModFeatures);
			} catch (final Exception e) {
				e.printStackTrace();
				AntiLaby.LOG.error("Failed to send AntiLaby packages to player "
						+ player.getName() + " (" + player.getUniqueId() + "): " + e.getMessage());
				return false;
			}
		return true;
	}

	public Map<LabyModFeature, Boolean> allowEverything() {
		final FileConfiguration cfg = ConfigFile.getCfg();
		final Map<LabyModFeature, Boolean> disabledLabyModFeaturesConfig = new EnumMap<>(LabyModFeature.class);
		// New features/Enable
		disabledLabyModFeaturesConfig.put(LabyModFeature.BLOCKBUILD,
				cfg.getBoolean("AntiLaby.Features.Enable.BLOCKBUILD"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.IMPROVED_LAVA,
				cfg.getBoolean("AntiLaby.Features.Enable.IMPROVED_LAVA"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.CROSSHAIR_SYNC,
				cfg.getBoolean("AntiLaby.Features.Enable.CROSSHAIR_SYNC"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.REFILL_FIX,
				cfg.getBoolean("AntiLaby.Features.Enable.REFILL_FIX"));
		return disabledLabyModFeaturesConfig;
	}

	public AntiLabyPackager setDisabledLabyModFeatures(Map<LabyModFeature, Boolean> disabledLabyModFeatures) {
		this.disabledLabyModFeatures = disabledLabyModFeatures;
		return this;
	}

	public AntiLabyPackager setForceIgnoreBypassPermission(boolean forceIgnoreBypassPermission) {
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
		return this;
	}

}
