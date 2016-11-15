package com.github.nathannr.antilaby.main.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.nathannr.antilaby.main.AntiLaby;

public class PlayerJoin implements Listener {

	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		//Disable LabyMod functions for the new player
		AntiLaby.instance.sendPackages(p);
	}
	
}
