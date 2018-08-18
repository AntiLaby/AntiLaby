package com.github.antilaby.antilaby.api.config;

/**
 * Get the values of the configuration file from the AutoUpdate section.
 *
 * @author NathanNr
 */
public class ConfigReaderAutoUpdate {

    public boolean autoUpdateReleaseEnabled() {
        return ConfigFile.getCfg().getBoolean("AntiLaby.Update.AutoUpdate.Release");
    }

    public boolean autoUpdateBetaEnabled() {
        return ConfigFile.getCfg().getBoolean("AntiLaby.Update.AutoUpdate.Beta");
    }

}
