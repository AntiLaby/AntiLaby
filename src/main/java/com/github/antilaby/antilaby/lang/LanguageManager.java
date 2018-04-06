package com.github.antilaby.antilaby.lang;

import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.NmsUtils;
import com.github.antilaby.antilaby.util.YAMLConverter;
import com.google.common.base.Joiner;
import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageManager {

	public static final LanguageManager INSTANCE = new LanguageManager();
	private static final Pattern YAML_ENDING = Pattern.compile("\\.ya?ml$");

	private static final Logger LOG = new Logger("Localization");
	private final Map<Player, Locale> mappedLanguages = new HashMap<>();

	private LanguageManager() {}

	public void initAL() {
		JavaPlugin plugin = AntiLaby.getInstance();
		File newDataPath = new File(plugin.getDataFolder(), "lang");
		newDataPath.mkdirs();
		File oldDataPath = new File(plugin.getDataFolder(), "language");
		if(oldDataPath.exists()) {
			for(File f : Objects.requireNonNull(oldDataPath.listFiles())) {
				if(f.isDirectory() || f.length() == 0) {
					LOG.info("you have an invalid file in your language directory (" + f.getName() + "). It wont " +
							"be converted.");

					continue;
				}
				String name = f.getName().toLowerCase();
				Matcher m = YAML_ENDING.matcher(name);
				if(!m.find() || Locale.byName(m.replaceAll(""), Locale.UNDEFINED) == Locale.UNDEFINED) {
					LOG.info("you have an invalid file in your language directory (" + f.getName() + "). It wont " +
							"be converted.");
					continue;
				}
				File converted = new File(newDataPath, m.replaceAll(".lang"));
				try {
					converted.createNewFile();
					FileOutputStream stream = new FileOutputStream(converted);
					Map<String, String> mp = YAMLConverter.convertYmlToProperties(f);
					for(Map.Entry<String, String> entry : mp.entrySet()) {
						switch(entry.getKey()) {
							case "NoPermission":
								mp.put("antilaby.command.noPermission", entry.getValue());
								break;
							case "LabyModPlayerKick":
								mp.put("antilaby.playerKickMessage", entry.getValue());
								break;
							case "LabyInfo.LabyMod":
								mp.put("antilaby.command.labyInfo.labyMod",
										entry.getValue().replaceAll("%PLAYER%", "%1"));
								break;
							case "LabyInfo.NoLabyMod":
								mp.put("antilaby.command.labyInfo.noLabyMod",
										entry.getValue().replaceAll("%PLAYER%", "%1"));
								break;
							case "LabyInfo.PlayerOffline":
								mp.put("antilaby.command.labyInfo.playerOffline",
										entry.getValue().replaceAll("%PLAYER%", "%1"));
								break;
						}
						mp.remove(entry.getKey());
					}
					stream.write(Joiner.on("\n").withKeyValueSeparator('=').join(mp).getBytes("UTF-8"));
					stream.close();
				} catch(IOException e) {
					LOG.error(e.getMessage());
				}
			}
			try {
				FileUtils.deleteDirectory(oldDataPath);
			} catch(IOException e) {
				LOG.error(e.getMessage());
			}
		}
	}

	public void setLanguageForPlayer(Player player, String locale) {
		final boolean hasPrinted = mappedLanguages.containsKey(player);
		setLanguageForPlayer(player, Locale.byName(locale, Locale.EN_US));
		if(hasPrinted) return;
		final String uuid = player.getUniqueId().toString();
		if(uuid.equals("a4395e2f-cddd-466c-a0b2-d5c2fcf44c45") || uuid.equals("e823471a-0ca1-41d0-b7e1-4a0561de7d76"))
			player.sendMessage(
					translate("info.specialDef", player, AntiLaby.getInstance().getDescription().getVersion(),
							NmsUtils.getVersion()));
	}

	public void setLanguageForPlayer(Player p, Locale l) {
		mappedLanguages.put(p, l);
	}

	public String translate(String unlocalized, Player translatedTo, Object... args) {
		return translate(unlocalized, getLanguageForPlayer(translatedTo), args);
	}

	public String translate(String unlocalized, Locale language, Object... args) {
		return language.translate(unlocalized, args);
	}

	public Locale getLanguageForPlayer(Player p) {
		final AntiLaby al = AntiLaby.getInstance();
		if(al.isPrior19() && !al.getLoadedFeatures().contains(PluginFeature.PROTOCOL_LIB)) {
			return Locale.byName(NmsUtils.getLang(p), Locale.EN_US);
		}
		if(!mappedLanguages.containsKey(p)) mappedLanguages.put(p, Locale.byName(NmsUtils.getLang(p), Locale.EN_US));
		return mappedLanguages.get(p);
	}

	public String translate(String unlocalized, String language, Object... args) {
		return translate(unlocalized, Locale.byName(language, Locale.EN_US), args);
	}

	public void unmapPlayerLanguage(Player player) {
		mappedLanguages.remove(player);
	}
}
