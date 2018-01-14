package com.github.antilaby.antilaby.events;

import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

public class EventsPost18 implements Listener {

	@EventHandler
	public void onChangeLocale(PlayerLocaleChangeEvent event) {
		if (!AntiLaby.getInstance().loadedFeatures.contains(PluginFeature.PROTOCOL_LIB))
			LanguageManager.INSTANCE.setLanguageForPlayer(event.getPlayer(), event.getLocale());
	}
}
