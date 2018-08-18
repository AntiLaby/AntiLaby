package com.github.antilaby.antilaby.api.config;

/**
 * Get the values of the configuration file from the LabyModPlayerAction section.
 *
 * @author NathanNr
 */
public class ConfigReaderLabyModPlayerAction {

    public boolean getLabyModPlayerKickEnabled() {
        return ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerAction.Kick.Enabled");
    }

    public boolean getLabyModPlayerBanEnabled() {
        return ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerAction.Ban.Enabled");
    }

}
