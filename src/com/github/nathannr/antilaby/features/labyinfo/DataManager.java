package com.github.nathannr.antilaby.features.labyinfo;

/*import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.Bukkit;

import com.github.nathannr.antilaby.main.AntiLaby;
import com.github.nathannr.antilaby.pluginchannel.IncomingPluginChannel;*/

public class DataManager {

	//private static String dataFilePath = AntiLaby.getInstance().getDataFolder() + "/labyinfo.ser";

	public static void saveData() {
		/*FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(dataFilePath);
			oos = new ObjectOutputStream(fos);
			LabyInfoPlayerPack lipp = new LabyInfoPlayerPack(IncomingPluginChannel.getLabyModPlayers());
			oos.writeObject(lipp);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}*/
	}

	public static void loadData() {
		/*if (Bukkit.getOnlinePlayers() != null) {
			ObjectInputStream ois = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(dataFilePath);
				ois = new ObjectInputStream(fis);
				Object obj = ois.readObject();
				if (obj instanceof LabyInfoPlayerPack) {
					LabyInfoPlayerPack lipp = (LabyInfoPlayerPack) obj;
					IncomingPluginChannel.setLabyModPlayers(lipp.getPlayers());
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException e) {
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}*/
	}

}
