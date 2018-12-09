package com.github.antilaby.antilaby.lang;

import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.NmsUtils;
import com.github.antilaby.antilaby.util.YamlConverter;
import com.google.common.base.Joiner;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LanguageManager {

  public static final LanguageManager INSTANCE = new LanguageManager();
  private static final Pattern YAML_ENDING = Pattern.compile("\\.ya?ml$");

  static final Logger LOG = new Logger("Localization");
  private final Map<Player, Locale> mappedLanguages = new HashMap<>();

  private LanguageManager() {}

  public void initAL() throws IOException {
    Path dataFolder = AntiLaby.getInstance().getDataPath();
    Path newDataPath = dataFolder.resolve("lang");
    if(!Files.exists(newDataPath)) {
      LOG.info("lang dir does not exist yet, creating...");
      Files.createDirectories(newDataPath);
    } else if(!Files.isDirectory(newDataPath)) {
      LOG.warn("There is a file called \"lang\", its not a dir, deleting!");
      Files.delete(newDataPath);
      Files.createDirectories(newDataPath);
    }
    Path oldDataPath = dataFolder.resolve("language");
    if(Files.isDirectory(oldDataPath)) {
      for(Path path : Files.walk(oldDataPath).sorted(Comparator.reverseOrder()).collect(Collectors.toList())) {
        if(Files.isDirectory(path)) {
          Files.delete(path);
          continue;
        }
        Path rel = oldDataPath.relativize(path);
        if(Files.size(path) == 0) {
          LOG.warn("Ignoring empty File " + rel);
          Files.delete(path);
          continue;
        }
        String name = path.getFileName().toString().toLowerCase();
        Matcher m = YAML_ENDING.matcher(name);
        if(!m.find() || Locale.byName(m.replaceAll(""), Locale.UNDEFINED) == Locale.UNDEFINED) {
          LOG.info(
              "you have an invalid file in your language directory (" + rel + "). It wont be converted" + ".");
          Files.delete(path);
          continue;
        }
        Path converted = newDataPath.resolve(m.replaceAll(".lang"));
        try {
          Map<String, String> mp = YamlConverter.convertYmlToProperties(path);
          Map<String, String> newKeys = new HashMap<>(5);
          for(Map.Entry<String, String> entry : mp.entrySet()) {
            switch(entry.getKey()) {
              case "NoPermission":
                newKeys.put("antilaby.command.noPermission", entry.getValue());
                break;
              case "LabyModPlayerKick":
                newKeys.put("antilaby.playerKickMessage", entry.getValue());
                break;
              case "LabyInfo.LabyMod":
                newKeys.put("antilaby.command.labyInfo.labyMod",
                    entry.getValue().replaceAll("%PLAYER%", "%1"));
                break;
              case "LabyInfo.NoLabyMod":
                newKeys.put("antilaby.command.labyInfo.noLabyMod",
                    entry.getValue().replaceAll("%PLAYER%", "%1"));
                break;
              case "LabyInfo.PlayerOffline":
                newKeys.put("antilaby.command.labyInfo.playerOffline",
                    entry.getValue().replaceAll("%PLAYER%", "%1"));
                break;
            }
          }
          Files.write(converted, Joiner.on("\n").withKeyValueSeparator('=').join(newKeys).getBytes("UTF-8"));
        } catch(YAMLException e) {
          LOG.warn("Could not convert '" + rel + "', " + e.getMessage());
        }
        Files.delete(path);
      }
      for(Locale locale : Locale.values()) locale.init();
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
