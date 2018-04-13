package com.github.antilaby.antilaby.compat;

import com.github.antilaby.antilaby.log.Logger;

/**
 * Defines a feature relying on another plug-in
 */
public enum PluginFeature {

	/** The ProtocolLib specific features */
	PROTOCOL_LIB, /** HeisluftsLanguageSystem specific features */
	HEISLUFTS_LANGUAGE_SYSTEM;

	static final Logger LOG = new Logger("Compat");

}
