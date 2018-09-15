package com.github.antilaby.antilaby.api.config;

/**
 * Get the values of the configuration file from the LabyMod Player Ban section.
 *
 * @author NathanNr
 */
public class ConfigReaderLabyModPlayerActionBan {

	public boolean isEnabled() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerAction.Ban.Enable");
	}

	/**
	 * Get the LabyMod Player Ban ban method
	 *
	 * @return ban method
	 */
	public LabyModPlayerBanMethod getMethod() {
	/*	switch (ConfigFile.getCfg().getString("AntiLaby.LabyModPlayerAction.Ban.Method.Method")) {
			case "built-in":
				return LabyModPlayerBanMethod.BUILT_IN;
			case "vanilla":
				return LabyModPlayerBanMethod.VANILLA;
			case "custom":
				return LabyModPlayerBanMethod.CUSTOM;
			default:
				return LabyModPlayerBanMethod.UNKNOWN;
		} */
		return LabyModPlayerBanMethod.CUSTOM;
	}

	public String getCustomCommand() {
		return ConfigFile.getCfg().getString("AntiLaby.LabyModPlayerAction.Ban.MethodCustom.Command");
	}

}
