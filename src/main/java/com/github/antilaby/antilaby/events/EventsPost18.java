package com.github.antilaby.antilaby.events;

import com.github.antilaby.antilaby.lang.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

/**
 * Handle if players change their local
 *
 * @author heisluft
 */
public class EventsPost18 implements Listener {

	@EventHandler
	public void onChangeLocale(PlayerLocaleChangeEvent event) {
		if(AntiLaby.getInstance().getLoadedFeatures().isEmpty())
			LanguageManager.INSTANCE.setLanguageForPlayer(event.getPlayer(), event.getLocale());
	}
}
