package com.github.antilaby.antilaby.compat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.github.antilaby.antilaby.lang.LanguageManager;

import static com.github.antilaby.antilaby.compat.PluginFeature.LOG;

import com.github.antilaby.antilaby.main.AntiLaby;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

/**
 * The ProtocolLib support class
 *
 * @author heisluft
 */
public class ProtocolLibSupport implements PacketListener {


	/**
	 * Initializes the AntiLaby ProtocolLib support
	 */
	public static void init() {
		LOG.info(
				"Enabling Support for ProtocolLib version " + ProtocolLibrary.getPlugin().getDescription().getVersion
						());
		ProtocolLibrary.getProtocolManager().addPacketListener(new ProtocolLibSupport());
	}

	@Override
	public Plugin getPlugin() {
		return AntiLaby.getInstance();
	}

	@Override
	public ListeningWhitelist getReceivingWhitelist() {
		return ListeningWhitelist.newBuilder().types(PacketType.fromName("SETTINGS")).build();
	}

	@Override
	public ListeningWhitelist getSendingWhitelist() {
		return ListeningWhitelist.EMPTY_WHITELIST;
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
		final Object handle = event.getPacket().getHandle();
		final LanguageManager lm = LanguageManager.INSTANCE;
		final Player p = event.getPlayer();
		final String old = lm.getLanguageForPlayer(p) != null ? lm.getLanguageForPlayer(p).name().toLowerCase(
				Locale.ROOT) : "";
		try {
			final String lang = (String) handle.getClass().getMethod("a").invoke(handle);
			if(!old.equals(lang)) lm.setLanguageForPlayer(event.getPlayer(), lang);

		} catch(final ReflectiveOperationException e) {
			LOG.error("could not retrieve language, falling back to possibly inaccurate events: " + e.getMessage());
		}
	}

	@Override
	public void onPacketSending(PacketEvent event) {}
}
