package com.github.nathannr.antilaby.main.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.nathannr.antilaby.main.AntiLaby;

public class PlayerJoin implements Listener {

	private AntiLaby plugin;
	public PlayerJoin(AntiLaby plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		//Disable LabyMod functions for the new player
		plugin.sendPackages(p);
	}
	
}
