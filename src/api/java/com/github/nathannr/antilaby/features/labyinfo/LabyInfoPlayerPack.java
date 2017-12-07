package com.github.nathannr.antilaby.features.labyinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class LabyInfoPlayerPack implements Serializable {

	private static final long serialVersionUID = -3475901410784292626L;

	private HashMap<String, String> players = new HashMap<>();

	public LabyInfoPlayerPack(HashMap<String, String> players) {
		this.players = players;
	}

	public LabyInfoPlayerPack(ArrayList<Player> players) {
		for(Player all : players) {
			this.players.put(all.getUniqueId().toString(), all.getName());
		}
	}
	
	public HashMap<String, String> getPlayers() {
		return this.players;
	}

}
