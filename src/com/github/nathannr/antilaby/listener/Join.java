package com.github.nathannr.antilaby.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.nathannr.antilaby.main.AntiLaby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Join implements Listener {

	private AntiLaby plugin;
	public Join(AntiLaby plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		plugin.sendPackages(p);
		if(p.isOp()) {
			if(plugin.getConfig().getBoolean("AntiLaby.Update.UpdateNotification")) {
				plugin.playerJoinCheckUpdate(p);
			}
		}
	}
	
}
