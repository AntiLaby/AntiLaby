package de.heisluft.antilaby.lang;

import org.bukkit.entity.Player;

public interface IClientLanguageManager extends ILanguageManager {

	public Locale getLanguageForPlayer(Player p);
	
	public void setLanguageForPlayer(Player p, Locale l);
	
	public String translate(String unlocalized, Player translatedTo, Object... args);
}
