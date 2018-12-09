package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

public class DataManager {

  private static final File dataFile = new File(AntiLaby.getInstance().getDataFolder() + "/labyinfo.ser");

  /**
   * Private constructor, no need to instantiate this class
   */
  private DataManager() {
    throw new UnsupportedOperationException();
  }

  /**
   * Loads all LabyPlayerInformation
   */
  public static void loadData() {
    if (dataFile.exists() && Bukkit.getOnlinePlayers().size() != 0) {
      ObjectInputStream ois = null;
      try {
        ois = new ObjectInputStream(new FileInputStream(dataFile));
        final HashMap<String, String> p = IOUtils.readObject(ois);
        for (final Entry<String, String> e : p.entrySet()) {
          System.out.println(e);
        }
        IncomingPluginChannel.setLabyModPlayers(p);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        IOUtils.closeSilently(ois);
      }
      cleanup();
    }
  }

  /**
   * Deletes the dataFile
   */
  public static void cleanup() {
    if (dataFile.exists()) {
      dataFile.delete();
    }
  }

  public static void saveData() {
    if (IncomingPluginChannel.getLabyModPlayers().isEmpty()) {
      return;
    }
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(new FileOutputStream(dataFile));
      oos.writeObject(IncomingPluginChannel.getLabyModPlayers());
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeSilently(oos);
    }
  }

}
