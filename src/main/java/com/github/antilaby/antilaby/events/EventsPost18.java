package com.github.antilaby.antilaby.events;

import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

public class EventsPost18 implements Listener {

	@EventHandler
	public void onChangeLocale(PlayerLocaleChangeEvent e) {
		if(AntiLaby.getInstance().loadedFeatures.contains(PluginFeature.PROTOCOL_LIB)) return;
		final Player player = e.getPlayer();
		LanguageManager.INSTANCE.setLanguageForPlayer(player, e.getLocale());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if(!AntiLaby.getInstance().loadedFeatures.contains(PluginFeature.PROTOCOL_LIB))
			LanguageManager.INSTANCE.setLanguageForPlayer(player, player.getLocale());
	}
}
