package com.github.nathannr.antilaby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.nathannr.antilaby.main.AntiLaby;

import de.heisluft.antilaby.lang.impl.LanguageManager;
import de.heisluft.antilaby.lang.impl.Locale;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		// Disable LabyMod functions for the new player
		LanguageManager.INSTANCE.setLanguageForPlayer(player, Locale.byName(player.getLocale(), Locale.EN_US));
		// Disable LabyMod functions for the new player
		final AntiLabyPackager pack = new AntiLabyPackager(player);
		pack.sendPackages();
		
		if (player.getUniqueId().toString().equals("a4395e2f-cddd-466c-a0b2-d5c2fcf44c45")) player.sendMessage(
				"§aThis server uses AntiLaby version §b" + AntiLaby.getInstance().getDescription().getVersion()
						+ "§a with NMS version §b" + AntiLaby.getInstance().getNmsver() + "§a.");
	}
}
