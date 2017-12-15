package de.heisluft.antilaby.lang.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.main.AntiLaby;

import de.heisluft.antilaby.lang.IClientLanguageManager;
import de.heisluft.antilaby.log.Logger;

public class LanguageManager implements IClientLanguageManager<Locale> {

	public static final LanguageManager INSTANCE = new LanguageManager();
	static final String RESOURCE_PATH = AntiLaby.getInstance().getDataFolder() + "/lang";
	static final int RESOURCE_VERSION = 1;

	static final Logger LOG = new Logger("Localization");
	private boolean isInit = false;

	private final Map<Player, Locale> mappedLanguages = new HashMap<>();
	
	private LanguageManager() {}
	
	@Override
	public Locale getLanguageForPlayer(Player p) {
		return mappedLanguages.get(p);
	}

	@Override
	public void init() {
		new File(RESOURCE_PATH).mkdir();
		for (final Locale l : Locale.values())
			l.init(false);
		for (final Player p : Bukkit.getOnlinePlayers())
			setLanguageForPlayer(p, p.getLocale());
		isInit = true;
	}

	@Override
	public boolean isInit() {
		return isInit;
	}
	
	@Override
	public void setLanguageForPlayer(Player p, Locale l) {
		mappedLanguages.put(p, l);
	}

	@Override
	public void setLanguageForPlayer(Player player, String locale) {
		final Locale l = Locale.byName(locale, Locale.EN_US);
		LOG.log(Level.INFO, "Setting the language of Player " + player.getName() + " to " + l.getName() + " (requested "
				+ locale + ")");
		final boolean hasPrinted = mappedLanguages.containsKey(player);
		setLanguageForPlayer(player, l);
		if (hasPrinted) return;
		final String uuid = player.getUniqueId().toString();
		if (uuid.equals("a4395e2f-cddd-466c-a0b2-d5c2fcf44c45") || uuid.equals("e823471a-0ca1-41d0-b7e1-4a0561de7d76"))
			player.sendMessage(translate("info.specialDef", player,
					AntiLaby.getInstance().getDescription().getVersion(), AntiLaby.getInstance().getNmsver()));
	}
	
	@Override
	public String translate(String unlocalized, Locale language, Object... args) {
		return language.translate(unlocalized, args);
	}
	
	@Override
	public String translate(String unlocalized, Player translatedTo, Object... args) {
		return translate(unlocalized, getLanguageForPlayer(translatedTo), args);
	}
	
	@Override
	public String translate(String unlocalized, String language, Object... args) {
		return translate(unlocalized, Locale.byName(language, Locale.EN_US), args);
	}

	public void unmapPlayerLanguage(Player player) {
		mappedLanguages.remove(player);
	}
}
