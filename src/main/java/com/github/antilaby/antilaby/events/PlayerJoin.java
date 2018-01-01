package com.github.antilaby.antilaby.events;

import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoin implements Listener {

	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		LanguageManager.INSTANCE.unmapPlayerLanguage(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Disable LabyMod functions for the new player
		new AntiLabyPackager(event.getPlayer()).sendPackages();
	}
}
