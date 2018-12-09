package com.github.antilaby.antilaby.events;

import com.github.antilaby.antilaby.updater.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.lang.LanguageManager;

/**
 * The player join event.
 */
public class PlayerJoin implements Listener {

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    LanguageManager.INSTANCE.unmapPlayerLanguage(event.getPlayer());
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    // Disable LabyMod functions for the new player
    new AntiLabyPackager(event.getPlayer()).sendPackages();
  }

}
