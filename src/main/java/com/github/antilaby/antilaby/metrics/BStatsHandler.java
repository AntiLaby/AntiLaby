package com.github.antilaby.antilaby.metrics;

import com.github.antilaby.antilaby.config.ConfigReader;
import com.github.antilaby.antilaby.AntiLaby;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.antilaby.antilaby.util.Constants;
import com.github.antilaby.antilaby.util.ServerHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

/**
 * A wrapper around the BStats API.
 */
public class BStatsHandler {
  /** The config reader. */
  private static final ConfigReader CONFIG_READER = new ConfigReader();

  /**
   * Initializes BStats charts and starts data transmission.
   */
  public static void initBStats() {
    final Metrics bstats = new Metrics(AntiLaby.getInstance());
    bstats.addCustomChart(new Metrics.SimplePie("autoupdate_enabled",
        () -> String.valueOf(CONFIG_READER.getAutoUpdate().release())));
    bstats.addCustomChart(new Metrics.SimplePie("is_bungee_server",
        () -> String.valueOf(ServerHelper.isBungeeCord())));
    bstats.addCustomChart(new Metrics.SimplePie("bypass_enabled",
        () -> String.valueOf(CONFIG_READER.getEnableBypassWithPermission())));
    bstats.addCustomChart(new Metrics.SimplePie("kick_enabled",
        () -> String.valueOf(CONFIG_READER.getLabyModPlayerAction().kickEnabled())));
    bstats.addCustomChart(new Metrics.SimplePie("download_source",
        () -> Constants.DOWNLOAD_SOURCE));
    // TODO add SimpleBarChart (if available at bStats)
    /*bstats.addCustomChart(new BStats.SimpleBarChart("disabled_functions", () -> {
      final Map<String, Integer> valueMap = new HashMap<>();
      final int food = Miscellaneous.boolToInt(Config.getFOOD());
      final int gui = Miscellaneous.boolToInt(Config.getGUI());
      final int nick = Miscellaneous.boolToInt(Config.getNICK());
      final int blockBuild = Miscellaneous.boolToInt(Config.getBLOCKBUILD());
      final int chat = Miscellaneous.boolToInt(Config.getCHAT());
      final int extras = Miscellaneous.boolToInt(Config.getEXTRAS());
      final int animations = Miscellaneous.boolToInt(Config.getANIMATIONS());
      final int potions = Miscellaneous.boolToInt(Config.getPOTIONS());
      final int armor = Miscellaneous.boolToInt(Config.getARMOR());
      final int damageIndicator = Miscellaneous.boolToInt(Config.getDAMAGEINDICATOR());
      final int minimapRadar = Miscellaneous.boolToInt(Config.getMINIMAP_RADAR());
      valueMap.put("FOOD", food);
      valueMap.put("GUI", gui);
      valueMap.put("NICK", nick);
      valueMap.put("BLOCKBUILD", blockBuild);
      valueMap.put("CHAT", chat);
      valueMap.put("EXTRAS", extras);
      valueMap.put("ANIMATIONS", animations);
      valueMap.put("POTIONS", potions);
      valueMap.put("ARMOR", armor);
      valueMap.put("DAMAGEINDICATOR", damageIndicator);
      valueMap.put("MINIMAP_RADAR", minimapRadar);
      return valueMap;
    }));*/
    bstats.addCustomChart(new Metrics.MultiLineChart("players_with_labymod_count", () -> {
      final Map<String, Integer> valueMap = new HashMap<>();
      valueMap.put("players_lm", IncomingPluginChannel.getLabyModPlayers().size());
      valueMap.put("players_no_lm",
          Bukkit.getOnlinePlayers().size() - IncomingPluginChannel.getLabyModPlayers().size());
      return valueMap;
    }));
    bstats.addCustomChart(new Metrics.SingleLineChart("players_with_labymod_count_single",
        () -> IncomingPluginChannel.getLabyModPlayers().size()));
    final List<String> labyModJoinCommands =
        CONFIG_READER.getLabyModPlayerAction().getJoinCommands(false);
    bstats.addCustomChart(new Metrics.SimplePie("lmjoincmd_enabled", () -> {
      if (labyModJoinCommands.isEmpty()) {
        return "false";
      } else {
        return "true";
      }
    }));
    bstats.addCustomChart(new Metrics.SingleLineChart("lmjoincmd_count",
        labyModJoinCommands::size));
  }

}
