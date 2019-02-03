package com.github.antilaby.antilaby.util;

import java.lang.reflect.Field;

/**
 * Provides Information about this Server.
 *
 * @author heisluft
 */
public final class ServerHelper {

  private static boolean isBungeeCord;
  private static ApiType type;
  private static ImplementationType implementation;

  /**
   * Private constructor, no need to instantiate this class.
   */
  private ServerHelper() {
    throw new UnsupportedOperationException();
  }

  /**
   * Get this servers Bukkit Api type.
   *
   * @return the APIType
   */
  public static ApiType getServerType() {
    if (type == null) {
      init();
    }
    return type;
  }

  /**
   * Get this servers Bukkit implementation.
   *
   * @return the ImplementationType
   */
  public static ImplementationType getImplementation() {
    if (implementation == null) {
      init();
    }
    return implementation;
  }

  /**
   * Resolves all fields.
   */
  private static void init() {
    try {
      Class.forName("net.glowstone.GlowServer");
      type = ApiType.PAPER_SPIGOT;
      implementation = ImplementationType.GLOWSTONE;
    } catch (ClassNotFoundException e) {
      try {
        Class.forName("org.bukkit.craftbukkit.Main");
        implementation = ImplementationType.CRAFT_BUKKIT;
      } catch (ClassNotFoundException e1) {
        implementation = ImplementationType.UNKNOWN;
      }
    }
    Class<?> spigotClass;
    try {
      spigotClass = Class.forName("org.spigotmc.SpigotConfig");
    } catch (ClassNotFoundException e) {
      type = ApiType.BUKKIT;
      isBungeeCord = false;
      return;
    }
    ApiType spigotType;
    try {
      Class.forName("com.destroystokyo.paper.PaperCommand");
      spigotType = ApiType.PAPER_SPIGOT;
    } catch (Exception e) {
      spigotType = ApiType.SPIGOT;
    }
    type = spigotType;
    boolean bungeeCord;
    try {
      Field field = spigotClass.getDeclaredField("bungee");
      field.setAccessible(true);
      bungeeCord = field.getBoolean(null);
    } catch (Exception e) {
      bungeeCord = false;
    }
    isBungeeCord = bungeeCord;
  }

  /**
   * Gets whether this Server is part of a BungeeCord network.
   *
   * @return true if User enabled the bungeeCord option in the servers spigot.yml
   */
  public static boolean isBungeeCord() {
    if (type == null) {
      init();
    }
    return isBungeeCord;
  }

  /**
   * The servers Bukkit implementation.
   */
  public enum ImplementationType {
    /**
     * Server is CraftBukkit based.
     */
    CRAFT_BUKKIT,
    /**
     * Server is Glowstone based.
     */
    GLOWSTONE,
    /**
     * Bukkit implementation is unknown.
     */
    UNKNOWN
  }

  /**
   * The servers Bukkit API type.
   */
  public enum ApiType {
    /**
     * Server only implements Bukkit API.
     */
    BUKKIT,
    /**
     * Server implements Bukkit + Spigot API.
     */
    SPIGOT,
    /**
     * Server implements Bukkit, Spigot + PaperSpigot API.
     */
    PAPER_SPIGOT
  }

}
