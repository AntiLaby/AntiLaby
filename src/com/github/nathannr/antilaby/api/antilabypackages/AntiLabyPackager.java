package com.github.nathannr.antilaby.api.antilabypackages;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyModFeature;
import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.api.util.Resource;
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
		boolean ignorePlayer;
		if (!forceIgnoreBypassPermission) {
			// Ignore players with the bypass permission if enabled in the
			// config file
			if (p.hasPermission("antilaby.bypass")) {
				if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.EnableBypassWithPermission")) {
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
			return false;
		}
		// TODO: Dont't forget to update the info file after adding a new
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
		HashMap<LabyModFeature, Boolean> disabledLabyModFeaturesConfig = new HashMap<LabyModFeature, Boolean>();
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.FOOD")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.FOOD, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.GUI")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.GUI, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.NICK")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.NICK, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.BLOCKBUILD")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.BLOCKBUILD, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.CHAT")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.CHAT, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.EXTRAS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.EXTRAS, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ANIMATIONS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.ANIMATIONS, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.POTIONS")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.POTIONS, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ARMOR")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.ARMOR, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.DAMAGEINDICATOR")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.DAMAGEINDICATOR, false);
		}
		if (AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.MINIMAP_RADAR")) {
			disabledLabyModFeaturesConfig.put(LabyModFeature.MINIMAP_RADAR, false);
		}
		return disabledLabyModFeaturesConfig;
	}

}
