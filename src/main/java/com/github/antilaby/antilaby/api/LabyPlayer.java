package com.github.antilaby.antilaby.api;

import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import org.bukkit.entity.Player;

public class LabyPlayer {
	
	/**
	 * Checks if a player uses LabyMod
	 *
	 * @param player
	 * 		the player to check
	 *
	 * @return true if the given Player uses LabyMod
	 */
	public static boolean doesPlayerUseLabyMod(Player player) {
		return (IncomingPluginChannel.getLabyModPlayers().containsKey(player.getUniqueId().toString()));
	}
	
}
