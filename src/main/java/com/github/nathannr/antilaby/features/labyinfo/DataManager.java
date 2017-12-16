package com.github.nathannr.antilaby.features.labyinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.github.nathannr.antilaby.main.AntiLaby;
import com.github.nathannr.antilaby.pluginchannel.IncomingPluginChannel;

import de.heisluft.antilaby.util.IOUtils;

public class DataManager {

	private static File dataFile = new File(AntiLaby.getInstance().getDataFolder() + "/labyinfo.ser");

	/**
	 * Deletes the dataFile
	 */
	public static void cleanup() {
		if (dataFile.exists()) dataFile.delete();
	}
	
	/**
	 * Loads all LabyPlayerInformation
	 */
	public static void loadData() {
		if (dataFile.exists() && Bukkit.getOnlinePlayers() != null) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(dataFile));
				final HashMap<String, String> p = IOUtils.<LabyInfoPlayerPack>readObject(ois).getPlayers();
				for (final Entry<String, String> e : p.entrySet())
					System.out.println(e);
				IncomingPluginChannel.setLabyModPlayers(p);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (ois != null) try {
					ois.close();
				} catch (final IOException e) {}
			}
			cleanup();
		}
	}

	public static void saveData() {
		if (IncomingPluginChannel.getLabyModPlayers().isEmpty()) return;
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(dataFile));
			final LabyInfoPlayerPack lipp = new LabyInfoPlayerPack(IncomingPluginChannel.getLabyModPlayers());
			oos.writeObject(lipp);
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) try {
				oos.close();
			} catch (final IOException e) {}
		}
	}

}
