package com.github.antilaby.antilaby.features.labyinfo;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Players {

	private static HashMap<Player, Boolean> players = new HashMap<>();
	
	public static HashMap<Player, Boolean> getPlayers() {
		return players;
	}
	
	public static void addPlayer(Player player, boolean usesLabyMod) {
		if(!players.containsKey(player))
			players.put(player, usesLabyMod);
	}
	
	public static int getNoLabyModPlayerCount() {
		int valueCount = 0;
        for (boolean b : players.values()) {
            if (b == false)
                valueCount++;
        }
        return valueCount;
    }
	
	public static int getLabyModPlayerCount() {
		int valueCount = 0;
        for (boolean b : players.values()) {
            if (b == true)
                valueCount++;
        }
        return valueCount;
    }
	
}
