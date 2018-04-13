package com.github.antilaby.antilaby.compat;

import com.github.antilaby.antilaby.main.AntiLaby;
import de.heisluft.lang.LanguageManager;

import static com.github.antilaby.antilaby.compat.PluginFeature.LOG;

public class HLSCompat {

	public static void init() {
		LOG.info("Enabling Support for HeisluftsLanguageSystem");
		LanguageManager.INSTANCE.registerPlugin(AntiLaby.getInstance());
	}

	private HLSCompat() {}

}
