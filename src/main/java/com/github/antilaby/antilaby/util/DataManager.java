package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

public class DataManager {

	private static final File dataFile = new File(AntiLaby.getInstance().getDataFolder() + "/labyinfo.ser");

	/**
	 * Loads all LabyPlayerInformation
	 */
	public static void loadData() {
		if(dataFile.exists() && Bukkit.getOnlinePlayers() != null) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(dataFile));
				final HashMap<String, String> p = IOUtils.readObject(ois);
				for(final Entry<String, String> e : p.entrySet())
					System.out.println(e);
				IncomingPluginChannel.setLabyModPlayers(p);
			} catch(IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if(ois != null) try {
					ois.close();
				} catch(final IOException e) {}
			}
			cleanup();
		}
	}

	/**
	 * Deletes the dataFile
	 */
	public static void cleanup() {
		if(dataFile.exists()) dataFile.delete();
	}

	public static void saveData() {
		if(IncomingPluginChannel.getLabyModPlayers().isEmpty()) return;
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(dataFile));
			oos.writeObject(IncomingPluginChannel.getLabyModPlayers());
		} catch(final IOException e) {
			e.printStackTrace();
		} finally {
			if(oos != null) try {
				oos.close();
			} catch(final IOException e) {}
		}
	}

	/**
	 * Private constructor, no need to instantiate this class
	 */
	private DataManager() {throw new UnsupportedOperationException();}

}
