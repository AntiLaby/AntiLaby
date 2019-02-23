package com.github.antilaby.antilaby.events;

import com.github.antilaby.antilaby.AntiLaby;
import com.github.antilaby.antilaby.lang.LanguageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

/**
 * Handles all events not present in mc version 1.8.
 *
 * @author heisluft
 */
public class EventsPost18 implements Listener {

  /**
   * Listen to the PlayerLocaleChangeEvent.
   *
   * @param event the event
   */
  @EventHandler
  public void onChangeLocale(PlayerLocaleChangeEvent event) {
    if (AntiLaby.getInstance().getLoadedFeatures().isEmpty()) {
      LanguageManager.INSTANCE.setLocale(event.getPlayer(), event.getLocale());
    }
  }
}
