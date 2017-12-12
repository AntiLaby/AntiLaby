package com.github.nathannr.antilaby.api;

import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.pluginchannel.IncomingPluginChannel;

public class LabyPlayer {
	
	private final Player player;

	public LabyPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Check if a player uses LabyMod
	 * 
	 * @return Uses LabyMod
	 * @author NathanNr
	 */
	public boolean usesLabyMod() {
		if (IncomingPluginChannel.getLabyModPlayers().containsKey(player.getUniqueId().toString())) return true;
		else return false;
	}

}
