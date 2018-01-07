package com.github.antilaby.antilaby.lang.impl;

import com.github.antilaby.antilaby.lang.IClientLanguageManager;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.NmsUtils;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager implements IClientLanguageManager<Locale> {

	public static final LanguageManager INSTANCE = new LanguageManager();
	static final String RESOURCE_PATH = AntiLaby.getInstance().getDataFolder() + "/lang";

	static final Logger LOG = new Logger("Localization");
	private final Map<Player, Locale> mappedLanguages = new HashMap<>();
	private boolean isInit = false;

	private LanguageManager() {}

	@Override
	public void init() {
		new File(RESOURCE_PATH).mkdirs();
		for(final Locale l : Locale.values())
			l.init(false);
		isInit = true;
	}

	@Override
	public void setLanguageForPlayer(Player player, String locale) {
		final Locale l = Locale.byName(locale, Locale.EN_US);
		final boolean hasPrinted = mappedLanguages.containsKey(player);
		setLanguageForPlayer(player, l);
		if(hasPrinted) return;
		final String uuid = player.getUniqueId().toString();
		if(uuid.equals("a4395e2f-cddd-466c-a0b2-d5c2fcf44c45") || uuid.equals("e823471a-0ca1-41d0-b7e1-4a0561de7d76"))
			player.sendMessage(translate("info.specialDef", player, AntiLaby.getInstance().getDescription().getVersion(), NmsUtils.getVersion()));
	}

	@Override
	public void setLanguageForPlayer(Player p, Locale l) {
		mappedLanguages.put(p, l);
	}

	@Override
	public String translate(String unlocalized, Player translatedTo, Object... args) {
		return translate(unlocalized, getLanguageForPlayer(translatedTo), args);
	}

	@Override
	public String translate(String unlocalized, Locale language, Object... args) {
		return language.translate(unlocalized, args);
	}

	@Override
	public Locale getLanguageForPlayer(Player p) {
		if(AntiLaby.getInstance().isPrior19()) {
			return Locale.byName(NmsUtils.getLang(p), Locale.EN_US);
		}
		return mappedLanguages.get(p);
	}

	@Override
	public boolean isInit() {
		return isInit;
	}

	@Override
	public String translate(String unlocalized, String language, Object... args) {
		return translate(unlocalized, Locale.byName(language, Locale.EN_US), args);
	}

	public void unmapPlayerLanguage(Player player) {
		mappedLanguages.remove(player);
	}
}
