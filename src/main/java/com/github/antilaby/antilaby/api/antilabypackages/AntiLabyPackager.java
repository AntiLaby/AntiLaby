package com.github.antilaby.antilaby.api.antilabypackages;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.config.ConfigFile;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.NmsUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;

public class AntiLabyPackager {

	private final Player p;
	private Map<LabyModFeature, Boolean> disabledLabyModFeatures = getConfigLabyModSettings();
	private boolean forceIgnoreBypassPermission = false;

	public AntiLabyPackager(Player p) {
		this.p = p;
	}

	public AntiLabyPackager(Player p, boolean forceIgnoreBypassPermission) {
		this.p = p;
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
	}

	public AntiLabyPackager(Player p, Map<LabyModFeature, Boolean> disabledLabyModFeatures) {
		this.p = p;
		this.disabledLabyModFeatures = disabledLabyModFeatures;
	}

	public AntiLabyPackager(Player p, Map<LabyModFeature, Boolean> disabledLabyModFeatures, boolean forceIgnoreBypassPermission) {
		this.p = p;
		this.disabledLabyModFeatures = disabledLabyModFeatures;
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
	}

	/**
	 * Get the AntiLaby packages from the config file
	 *
	 * @return AntiLaby packages from the config file
	 */
	public Map<LabyModFeature, Boolean> getConfigLabyModSettings() {

		final FileConfiguration cfg = ConfigFile.getCfg();

		final Map<LabyModFeature, Boolean> disabledLabyModFeaturesConfig = new EnumMap<>(LabyModFeature.class);

		// Old features:
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.FOOD"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.FOOD, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.FOOD, true);
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.GUI"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.GUI, true);
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.NICK"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.NICK, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.NICK, true);
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.EXTRAS"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.EXTRAS, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.EXTRAS, true);
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.POTIONS"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.POTIONS, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.POTIONS, true);
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.ARMOR"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.ARMOR, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.ARMOR, false);
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.DAMAGEINDICATOR, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.DAMAGEINDICATOR, true);
		if(cfg.getBoolean("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.MINIMAP_RADAR, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.MINIMAP_RADAR, true);

		// New features/Disable:
		if(cfg.getBoolean("AntiLaby.Features.Disable.SATURATION_BAR"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.SATURATION_BAR, false);
		if(cfg.getBoolean("AntiLaby.Features.Disable.CHAT"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.CHAT, false);
		if(cfg.getBoolean("AntiLaby.Features.Disable.GUI_ALL"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_ALL, false);
		if(cfg.getBoolean("AntiLaby.Features.Disable.GUI_POTION_EFFECTS"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_POTION_EFFECTS, false);
		if(cfg.getBoolean("AntiLaby.Features.Disable.GUI_ARMOR_HUD"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_ARMOR_HUD, false);
		if(cfg.getBoolean("AntiLaby.Features.Disable.GUI_ITEM_HUD"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_ITEM_HUD, false);
		if(cfg.getBoolean("AntiLaby.Features.Disable.TAGS"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.TAGS, false);
		if(cfg.getBoolean("AntiLaby.Features.Disable.ANIMATIONS"))
			disabledLabyModFeaturesConfig.put(LabyModFeature.ANIMATIONS, false);
		else disabledLabyModFeaturesConfig.put(LabyModFeature.ANIMATIONS, true);

		// New features/Enable
		disabledLabyModFeaturesConfig.put(LabyModFeature.BLOCKBUILD, cfg.getBoolean("AntiLaby.Features.Enable.BLOCKBUILD"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.IMPROVED_LAVA, cfg.getBoolean("AntiLaby.Features.Enable.IMPROVED_LAVA"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.CROSSHAIR_SYNC, cfg.getBoolean("AntiLaby.Features.Enable.CROSSHAIR_SYNC"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.REFILL_FIX, cfg.getBoolean("AntiLaby.Features.Enable.REFILL_FIX"));
		return disabledLabyModFeaturesConfig;
	}

	/**
	 * Send the AntiLaby packages
	 *
	 * @return success
	 */
	public boolean sendPackages() {
		final FileConfiguration cfg = ConfigFile.getCfg();
		boolean ignorePlayer = false;
		if(!forceIgnoreBypassPermission) // Ignore players with the bypass permission if enabled in the
			// config file
			if(p.hasPermission("antilaby.bypass")) if(cfg.getBoolean("AntiLaby.EnableBypassWithPermission")) {
				disabledLabyModFeatures = allowEverything();
				ignorePlayer = true;
				AntiLaby.LOG.debug("Player " + p.getName() + " (" + p.getUniqueId() + ") has the permission 'antilaby.bypass' and has been ignored.");
			}
		if(ignorePlayer) disabledLabyModFeatures = allowEverything();
		else if(!disabledLabyModFeatures.isEmpty()) try {
			NmsUtils.setLabyModFeature(p, disabledLabyModFeatures);
		} catch(final Exception e) {
			e.printStackTrace();
			AntiLaby.LOG.error("An unknown error has occurred: Can't send AntiLaby packages to player " + p.getName() + " (" + p.getUniqueId() + ")!");
			return false;
		}
		return true;
	}

	public Map<LabyModFeature, Boolean> allowEverything() {
		final FileConfiguration cfg = ConfigFile.getCfg();
		final Map<LabyModFeature, Boolean> disabledLabyModFeaturesConfig = new EnumMap<>(LabyModFeature.class);
		// New features/Enable
		disabledLabyModFeaturesConfig.put(LabyModFeature.BLOCKBUILD, cfg.getBoolean("AntiLaby.Features.Enable.BLOCKBUILD"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.IMPROVED_LAVA, cfg.getBoolean("AntiLaby.Features.Enable.IMPROVED_LAVA"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.CROSSHAIR_SYNC, cfg.getBoolean("AntiLaby.Features.Enable.CROSSHAIR_SYNC"));
		disabledLabyModFeaturesConfig.put(LabyModFeature.REFILL_FIX, cfg.getBoolean("AntiLaby.Features.Enable.REFILL_FIX"));
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
