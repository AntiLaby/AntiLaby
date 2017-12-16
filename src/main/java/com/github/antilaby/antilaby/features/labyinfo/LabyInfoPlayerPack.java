package com.github.antilaby.antilaby.features.labyinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class LabyInfoPlayerPack implements Serializable {

	private static final long serialVersionUID = -3475901410784292626L;

	private HashMap<String, String> playersLabyMod = new HashMap<>();

	public LabyInfoPlayerPack(HashMap<String, String> playersLabyMod) {
		this.playersLabyMod = playersLabyMod;
	}

	public LabyInfoPlayerPack(ArrayList<Player> playersLabyMod) {
		for(Player all : playersLabyMod) {
			this.playersLabyMod.put(all.getUniqueId().toString(), all.getName());
		}
	}
	
	public HashMap<String, String> getPlayers() {
		return this.playersLabyMod;
	}

}
