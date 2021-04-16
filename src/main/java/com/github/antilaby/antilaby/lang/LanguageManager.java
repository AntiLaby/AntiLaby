package com.github.antilaby.antilaby.lang;

import com.github.antilaby.antilaby.AntiLaby;
import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.util.FeatureProvider;
import com.github.antilaby.antilaby.util.YamlConverter;
import com.google.common.base.Joiner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.error.YAMLException;

public class LanguageManager {
  /** The singleton instance. */
  public static final LanguageManager INSTANCE = new LanguageManager();
  /** The localization logger. */
  static final Logger LOG = new Logger("Localization");
  /** Regex Pattern to recognize YAML files. */
  private static final Pattern YAML_ENDING = Pattern.compile("\\.ya?ml$");
  /** All mapped languages. */
  private final Map<Player, Locale> mappedLanguages = new HashMap<>();

  /**
   * Instantiates the singleton instance.
   *
   * @throws IllegalStateException if the instance already was created
   */
  private LanguageManager() {
    if (INSTANCE != null) {
      throw new IllegalStateException("LanguageManager is a singleton");
    }
  }

  /**
   * Like {@link Bukkit#broadcast(String, String)}, but uses translations and a prefix.
   *
   * @param unlocalized the translation key
   * @param permission the permission each player has to have to get sent the message
   * @param prefix the prefix to send
   * @param args the arguments to replace with
   */
  public void broadcast(String unlocalized, String permission, String prefix, Object... args) {
    Bukkit.getOnlinePlayers().forEach(player -> {
      if (player.hasPermission(permission)) {
        player.sendMessage(translate(unlocalized, player, args));
      }
    });
  }

  /**
   * Like {@link Bukkit#broadcastMessage(String)}, but uses translations.
   *
   * @param unlocalized the translation key
   * @param args the arguments to replace with
   */
  public void broadcast(String unlocalized, Object... args) {
    Bukkit.getOnlinePlayers().forEach(player ->
        player.sendMessage(translate(unlocalized, player, args)));
  }

  /**
   * Like {@link Bukkit#broadcast(String, String)}, but uses translations.
   *
   * @param unlocalized the translation key
   * @param permission the permission each player has to have to get sent the message
   * @param args the arguments to replace with
   */
  public void broadcast(String unlocalized, String permission, Object... args) {
    Bukkit.getOnlinePlayers().forEach(player -> {
      if (player.hasPermission(permission)) {
        player.sendMessage(translate(unlocalized, player, args));
      }
    });
  }

  /**
   * Initializes all AntiLaby translations and converts old yaml files to new system.
   * This operation is atomic by any means; if it fails, you will likely have to check every file
   * for validity (based on the exception message).
   *
   * @throws IOException if any IO operation did not succeed
   */
  public void initTranslations() throws IOException {
    Path dataFolder = AntiLaby.getInstance().getDataPath();
    Path newDataPath = dataFolder.resolve("lang");
    if (!Files.exists(newDataPath)) {
      LOG.info("lang dir does not exist yet, creating...");
      Files.createDirectories(newDataPath);
    } else if (!Files.isDirectory(newDataPath)) {
      LOG.warn("There is a file called \"lang\", its not a dir, deleting!");
      Files.delete(newDataPath);
      Files.createDirectories(newDataPath);
    }
    Path oldDataPath = dataFolder.resolve("language");
    if (Files.isDirectory(oldDataPath)) {
      for (Path path :
          Files.walk(oldDataPath).sorted(Comparator.reverseOrder()).collect(Collectors.toList())) {
        if (Files.isDirectory(path)) {
          Files.delete(path);
          continue;
        }
        Path rel = oldDataPath.relativize(path);
        if (Files.size(path) == 0) {
          LOG.warn("Ignoring empty File " + rel);
          Files.delete(path);
          continue;
        }
        String name = path.getFileName().toString().toLowerCase();
        Matcher m = YAML_ENDING.matcher(name);
        if (!m.find() || Locale.byName(m.replaceAll(""), Locale.UNDEFINED) == Locale.UNDEFINED) {
          LOG.info("you have an invalid file in your language directory (" + rel + "). It wont be"
              + " converted" + ".");
          Files.delete(path);
          continue;
        }
        Path converted = newDataPath.resolve(m.replaceAll(".lang"));
        try {
          Map<String, String> mp = YamlConverter.convertYmlToProperties(path);
          Map<String, String> newKeys = new HashMap<>(5);
          for (Map.Entry<String, String> entry : mp.entrySet()) {
            switch (entry.getKey()) {
              case "NoPermission":
                newKeys.put("antilaby.command.noPermission", entry.getValue());
                break;
              case "LabyModPlayerKick":
                newKeys.put("antilaby.playerKickMessage", entry.getValue());
                break;
              case "LabyInfo.LabyMod":
                newKeys.put("antilaby.command.labyInfo.labyMod", entry.getValue().replaceAll(
                    "%PLAYER%", "%1"));
                break;
              case "LabyInfo.NoLabyMod":
                newKeys.put("antilaby.command.labyInfo.noLabyMod", entry.getValue().replaceAll(
                    "%PLAYER%", "%1"));
                break;
              case "LabyInfo.PlayerOffline":
                newKeys.put("antilaby.command.labyInfo.playerOffline",
                    entry.getValue().replaceAll("%PLAYER%", "%1"));
                break;
              default:
                LOG.warn("Language file " + name + " contained unknown key: " + entry.getKey());
            }
          }
          Files.write(converted,
              Joiner.on("\n").withKeyValueSeparator('=').join(newKeys).getBytes("UTF-8"));
        } catch (YAMLException e) {
          LOG.warn("Could not convert '" + rel + "', " + e.getMessage());
        }
        Files.delete(path);
      }
    }
    try {
      for (Locale locale : Locale.values()) {
        locale.init();
      }
    } finally {
      Locale.closeFileSys();
    }
  }

  /**
   * (Re-)set a Player - Locale mapping.
   * This determines in what translation {@link #translate(String, Player, Object...)} results.
   * Same as calling {@link #setLocale(Player, Locale)} with {@code player} and
   * {@link Locale#byName(String, Locale)} of {@code language} and {@link Locale#EN_US} as fallback.
   *
   * @param player the player to set the locale for
   * @param locale the name of the locale to set
   * @see #setLocale(Player, Locale)
   */
  public void setLocale(Player player, String locale) {
    final boolean hasPrinted = mappedLanguages.containsKey(player);
    setLocale(player, Locale.byName(locale, Locale.EN_US));
    if (hasPrinted) {
      return;
    }
    final String uuid = player.getUniqueId().toString();
    if (uuid.equals("a4395e2f-cddd-466c-a0b2-d5c2fcf44c45")
        || uuid.equals("e823471a-0ca1-41d0-b7e1-4a0561de7d76")) {
      player.sendMessage(translate("info.specialDef", player,
          AntiLaby.getInstance().getDescription().getVersion(),
          Bukkit.getServer().getVersion()));
    }
  }

  /**
   * (Re-)set a Player - Locale mapping.
   * This determines in what translation {@link #translate(String, Player, Object...)} results
   *
   * @param player the player to set the locale for
   * @param locale the locale to set
   */
  public void setLocale(Player player, Locale locale) {
    mappedLanguages.put(player, locale);
  }

  /**
   * Get the mapped Locale of a specified player.
   *
   * @param player the player to get the language for
   * @return the mapped locale
   */
  public Locale getLanguageForPlayer(Player player) {
    final AntiLaby al = AntiLaby.getInstance();
    if (al.isChangeLangUnavailable() && !al.getLoadedFeatures()
        .contains(PluginFeature.PROTOCOL_LIB)) {
      return FeatureProvider.getLanguage(player);
    }
    if (!mappedLanguages.containsKey(player)) {
      mappedLanguages.put(player, FeatureProvider.getLanguage(player));
    }
    return mappedLanguages.get(player);
  }

  /**
   * Removes a Player - Locale mapping.
   *
   * @param player the player to remove the mapping for.
   */
  public void unsetLocale(Player player) {
    mappedLanguages.remove(player);
  }

  /**
   * Translate a translation key to a language by name.
   * Same as calling {@link Locale#translate(String, Object...)} with {@code unlocalized} and
   * {@code args} on {@link Locale#byName(String, Locale)}
   * with args {@code language} and {@link Locale#EN_US} as fallback.
   *
   * @param unlocalized the translation key.
   * @param language the name of the language to which to translate the translation key
   * @param args the arguments to replace with
   * @return the localized message
   * @see Locale#translate(String, Object...)
   * @see Locale#byName(String, Locale)
   */
  public String translate(String unlocalized, String language, Object... args) {
    return translate(unlocalized, Locale.byName(language, Locale.EN_US), args);
  }

  /**
   * Translate a translation key to the mapped locale of a player.
   * Same as calling {@link Locale#translate(String, Object...)} with {@code unlocalized} and
   * {@code args} on {@link #getLanguageForPlayer(Player)} with {@code translatedTo} as arg.
   *
   * @param unlocalized the translation key.
   * @param translatedTo the player to which to translate the translation key
   * @param args the arguments to replace with
   * @return the localized message
   * @see Locale#translate(String, Object...)
   * @see #getLanguageForPlayer(Player)
   */
  public String translate(String unlocalized, Player translatedTo, Object... args) {
    return translate(unlocalized, getLanguageForPlayer(translatedTo), args);
  }

  /**
   * Translate a translation key to a given locale.
   * Same as calling {@link Locale#translate(String, Object...)} with {@code unlocalized} and
   * {@code args} on {@code language}.
   *
   * @param unlocalized the translation key.
   * @param locale locale to which to translate
   * @param args the arguments to replace with
   * @return the localized message
   * @see Locale#translate(String, Object...)
   */
  public String translate(String unlocalized, Locale locale, Object... args) {
    return locale.translate(unlocalized, args);
  }
}
