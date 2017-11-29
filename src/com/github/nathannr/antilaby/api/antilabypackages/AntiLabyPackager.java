package com.github.nathannr.antilaby.api.antilabypackages;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyModFeature;
import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.api.util.Resource;
import com.github.nathannr.antilaby.config.ConfigFile;
import com.github.nathannr.antilaby.main.AntiLaby;
import com.github.nathannr.antilaby.nms.Nms_v1_10_R1;
import com.github.nathannr.antilaby.nms.Nms_v1_11_R1;
import com.github.nathannr.antilaby.nms.Nms_v1_12_R1;
import com.github.nathannr.antilaby.nms.Nms_v1_8_R1;
import com.github.nathannr.antilaby.nms.Nms_v1_8_R2;
import com.github.nathannr.antilaby.nms.Nms_v1_8_R3;
import com.github.nathannr.antilaby.nms.Nms_v1_9_R1;
import com.github.nathannr.antilaby.nms.Nms_v1_9_R2;

public class AntiLabyPackager {

	private Player p;
	private HashMap<LabyModFeature, Boolean> disabledLabyModFeatures = getConfigLabyModSettings();
	private boolean forceIgnoreBypassPermission = false;

	public AntiLabyPackager(Player p) {
		this.p = p;
	}

	public AntiLabyPackager(Player p, HashMap<LabyModFeature, Boolean> disabledLabyModFeatures) {
		this.p = p;
		this.disabledLabyModFeatures = disabledLabyModFeatures;
	}

	public AntiLabyPackager(Player p, boolean forceIgnoreBypassPermission) {
		this.p = p;
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
	}

	public AntiLabyPackager(Player p, HashMap<LabyModFeature, Boolean> disabledLabyModFeatures,
			boolean forceIgnoreBypassPermission) {
		this.p = p;
		this.disabledLabyModFeatures = disabledLabyModFeatures;
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
	}

	public boolean setForceIgnoreBypassPerission(boolean forceIgnoreBypassPermission) {
		this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
		return true;
	}

	public boolean setDisabledLabyModFeatures(HashMap<LabyModFeature, Boolean> disabledLabyModFeatures) {
		this.disabledLabyModFeatures = disabledLabyModFeatures;
		return true;
	}

	/**
	 * Send the AntiLaby packages
	 * 
	 * @return success
	 * @author NathanNr
	 */
	public boolean sendPackages() {
		FileConfiguration cfg = ConfigFile.getCfg();
		boolean ignorePlayer;
		if (!forceIgnoreBypassPermission) {
			// Ignore players with the bypass permission if enabled in the
			// config file
			if (p.hasPermission("antilaby.bypass")) {
				if (cfg.getBoolean("AntiLaby.EnableBypassWithPermission")) {
					this.disabledLabyModFeatures = this.allowEverything();
					ignorePlayer = true;
					System.out.println("[AntiLaby/INFO] Player " + p.getName() + " (" + p.getUniqueId()
							+ ") has the permission 'antilaby.bypass' and has been ignored.");
				} else {
					ignorePlayer = false;
				}
			} else {
				ignorePlayer = false;
			}
		} else {
			ignorePlayer = false;
		}
		if (ignorePlayer) {
			this.disabledLabyModFeatures = this.allowEverything();
		}
		// TODO: Don't forget to update the info file after adding a new
		// NMS-version!
		try {
			if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_8_R1")) {
				Nms_v1_8_R1.setLabyModFeature(p, disabledLabyModFeatures);
			} else if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_8_R2")) {
				Nms_v1_8_R2.setLabyModFeature(p, disabledLabyModFeatures);
			} else if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_8_R3")) {
				Nms_v1_8_R3.setLabyModFeature(p, disabledLabyModFeatures);
			} else if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_9_R1")) {
				Nms_v1_9_R1.setLabyModFeature(p, disabledLabyModFeatures);
			} else if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_9_R2")) {
				Nms_v1_9_R2.setLabyModFeature(p, disabledLabyModFeatures);
			} else if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_10_R1")) {
				Nms_v1_10_R1.setLabyModFeature(p, disabledLabyModFeatures);
			} else if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_11_R1")) {
				Nms_v1_11_R1.setLabyModFeature(p, disabledLabyModFeatures);
			} else if (AntiLaby.getInstance().getNmsver().equalsIgnoreCase("v1_12_R1")) {
				Nms_v1_12_R1.setLabyModFeature(p, disabledLabyModFeatures);
			} else {
				System.err.println(
						"[AntiLaby/ERROR] " + "Your server version is not compatible with this version of AntiLaby!");
				if (p.isOp()) {
					p.sendMessage(Prefix.PREFIX
							+ "§4AntiLaby is not compatible with your server version! A newer version of AntiLaby is maybe compatible with your server, check for updates here: "
							+ Resource.RESOURCE_LINK + "§r");
				}
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("[AntiLaby/ERROR] An unknown error has occurred: Can't send AntiLaby packages to player "
					+ p.getName() + " (" + p.getUniqueId() + ")!");
		}
		return true;
	}

	/**
	 * Get the AntiLaby packages from the config file
	 * 
	 * @return AntiLaby packages from the config file
	 * @author NathanNr
	 */
	public HashMap<LabyModFeature, Boolean> getConfigLabyModSettings() {

		FileConfiguration cfg = ConfigFile.getCfg();

		HashMap<LabyModFeature, Boolean> disabledLabyModFeaturesConfig = new HashMap<LabyModFeature, Boolean>();

		// Old features:
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.FOOD")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.FOOD, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.FOOD, true);
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.GUI")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI, true);
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.NICK")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.NICK, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.NICK, true);
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.EXTRAS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.EXTRAS, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.EXTRAS, true);
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.POTIONS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.POTIONS, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.POTIONS, true);
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.ARMOR")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.ARMOR, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.ARMOR, false);
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.DAMAGEINDICATOR, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.DAMAGEINDICATOR, true);
		if (cfg.getBoolean("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.MINIMAP_RADAR, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.MINIMAP_RADAR, true);

		// New features/Disable:
		if (cfg.getBoolean("AntiLaby.Features.Disable.SATURATION_BAR")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.SATURATION_BAR, false);
		}
		if (cfg.getBoolean("AntiLaby.Features.Disable.CHAT")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.CHAT, false);
		}
		if (cfg.getBoolean("AntiLaby.Features.Disable.GUI_ALL")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_ALL, false);
		}
		if (cfg.getBoolean("AntiLaby.Features.Disable.GUI_POTION_EFFECTS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_POTION_EFFECTS, false);
		}
		if (cfg.getBoolean("AntiLaby.Features.Disable.GUI_ARMOR_HUD")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_ARMOR_HUD, false);
		}
		if (cfg.getBoolean("AntiLaby.Features.Disable.GUI_ITEM_HUD")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI_ITEM_HUD, false);
		}
		if (cfg.getBoolean("AntiLaby.Features.Disable.TAGS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.TAGS, false);
		}
		if (cfg.getBoolean("AntiLaby.Features.Disable.ANIMATIONS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.ANIMATIONS, false);
		} else
			disabledLabyModFeaturesConfig.put(LabyModFeature.ANIMATIONS, true);

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

	public HashMap<LabyModFeature, Boolean> allowEverything() {
		FileConfiguration cfg = ConfigFile.getCfg();
		HashMap<LabyModFeature, Boolean> disabledLabyModFeaturesConfig = new HashMap<LabyModFeature, Boolean>();
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

}
