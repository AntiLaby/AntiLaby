package com.github.nathannr.antilaby.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.api.util.Resource;
import com.github.nathannr.antilaby.io.ReadUrl;
import com.github.nathannr.antilaby.main.AntiLaby;

public class UpdateDownloader extends Thread {

	private boolean updateAvailable;
	private String newVersion;

	@Override
	// Start update function async
	public void run() {
		// Check for updates
		System.out.println(Prefix.CPREFIXINFO + "Checking for updates on spigotmc.org...");
		try {
			this.newVersion = ReadUrl.readUrl("https://api.spigotmc.org/legacy/update.php?resource=" + Resource.RESOURCE_ID);
			if(newVersion != null) {
				if(!newVersion.contains(" ") || !newVersion.contains("!")) {
					if (!this.newVersion.equalsIgnoreCase(AntiLaby.getInstance().getDescription().getVersion())) {
						System.out.println(Prefix.CPREFIXINFO + "Update found! Version " + this.newVersion + " is available.");
						this.updateAvailable = true;
					} else {
						System.out.println(Prefix.CPREFIXINFO + "No update found. You are running the newest version.");
						this.updateAvailable = false;
					}
				} else {
					System.err.println(Prefix.CPREFIXERROR + "Failed to check for updates on spigotmc.org! (Invalid value received)");
					System.err.println(newVersion);
					this.updateAvailable = false;
				}
			} else {
				System.err.println(Prefix.CPREFIXERROR + "Failed to check for updates on spigotmc.org! (No information received)");
				this.updateAvailable = false;
			}
		} catch (Exception ex) {
			System.err.println(Prefix.CPREFIXERROR + "Failed to check for updates on spigotmc.org! (" + ex.getMessage() + ")");
			this.updateAvailable = false;
		}
		// Download and install update if available
		if (updateAvailable == true) {
			if (this.installUpdate(
					"https://github.com/NathanNr/AntiLaby/releases/download/" + this.newVersion + "/AntiLaby.jar", 1)) {
				System.out.println(Prefix.CPREFIXINFO
						+ "Auto-update complete! Reload or restart your server to activate the new version.");
			} else {
				System.err.println(Prefix.CPREFIXERROR + "Failed to download update from download server 1!");
				if (this.installUpdate("http://adf.ly/1f1ZEn", 2)) {
					System.out.println(Prefix.CPREFIXINFO
							+ "Auto-update complete! Reload or restart your server to activate the new version.");
				} else {
					System.err.println(Prefix.CPREFIXERROR
							+ "Failed to install update from download server 1 and 2! Please install the newest version manually from https://www.spigotmc.org/resources/"
							+ Resource.RESOURCE_ID + "/!");
				}
			}
			File tmp = new File("plugins/AntiLaby.tmp");
			if (tmp.exists()) {
				tmp.delete();
			}
		}
		AntiLaby.getInstance().disableIfNotCompatible();
		this.interrupt();
	}

	private boolean installUpdate(String urlString, int urlId) {
		System.out.println(Prefix.CPREFIXINFO + "Downloading update from download server " + urlId + "...");
		try {
			URL url = new URL(urlString);
			final URLConnection conn = url.openConnection();
			final InputStream is = new BufferedInputStream(conn.getInputStream());
			final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.tmp"));
			byte[] chunk = new byte[1024];
			int chunkSize;
			while ((chunkSize = is.read(chunk)) != -1) {
				os.write(chunk, 0, chunkSize);
			}
			os.close();
			File newfile = new File("plugins/AntiLaby.tmp");
			long newlength = newfile.length();
			if (newlength <= 10000) {
				newfile.delete();
				return false;
			} else {
				System.out.println(Prefix.CPREFIXINFO + "Installing update...");

				final FileInputStream is2 = new FileInputStream(new File("plugins/AntiLaby.tmp"));

				final OutputStream os2 = new BufferedOutputStream(
						new FileOutputStream(AntiLaby.getInstance().getPluginFile()));
				byte[] chunk2 = new byte[1024];
				int chunkSize2;
				while ((chunkSize2 = is2.read(chunk2)) != -1) {
					os2.write(chunk2, 0, chunkSize2);
				}
				is2.close();
				os2.close();

				File tmp = new File("plugins/AntiLaby.tmp");
				tmp.delete();
				return true;
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
