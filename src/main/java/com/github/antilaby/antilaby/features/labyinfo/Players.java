package com.github.antilaby.antilaby.features.labyinfo;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class Players {
	
	private static final HashMap<Player, Boolean> players = new HashMap<>();
	
	/**
	 * Get the full list of players and the information if they use LabyMod
	 *
	 * @return HashMap with all saved players
	 *
	 * @author NathanNr
	 */
	public static HashMap<Player, Boolean> getPlayers() {
		return players;
	}
	
	/**
	 * Add the LabyInfo-information of a player
	 *
	 * @param player
	 * 		The player
	 * @param usesLabyMod
	 * 		Uses the player LabyMod or not?
	 *
	 * @author NathanNr
	 */
	public static void addPlayer(Player player, boolean usesLabyMod) {
		if(!players.containsKey(player))
			players.put(player, usesLabyMod);
	}
	
	/**
	 * Get the count of the players who don't use LabyMod
	 *
	 * @return count
	 *
	 * @author NathanNr
	 */
	public static int getNoLabyModPlayerCount() {
		int valueCount = 0;
		for(boolean b : players.values()) {
			if(!b)
				valueCount++;
		}
		return valueCount;
	}
	
	/**
	 * Get the count of the players who use LabyMod
	 *
	 * @return count
	 *
	 * @author NathanNr
	 */
	public static int getLabyModPlayerCount() {
		int valueCount = 0;
		for(boolean b : players.values()) {
			if(b)
				valueCount++;
		}
		return valueCount;
	}
	
}
