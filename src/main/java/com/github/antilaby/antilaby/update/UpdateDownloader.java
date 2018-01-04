package com.github.antilaby.antilaby.update;

import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import com.github.antilaby.antilaby.util.IOUtils;

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

/**
 * The old auto-updater class. It will be removed in the future, if the new
 * auto-updater is ready for use.
 * 
 * @author NathanNr
 */

public class UpdateDownloader extends Thread {

	private boolean updateAvailable;
	private String newVersion;

	@Override
	// Start update function async
	public void run() {
		// Check for updates
		AntiLaby.LOG.debug("Checking for updates on spigotmc.org...");
		try {
			newVersion = IOUtils
					.readUrl("https://api.spigotmc.org/legacy/update.php?resource=" + Constants.RESOURCE_ID);
			if (newVersion != null) {
				if (!newVersion.contains(" ") || !newVersion.contains("!")) {
					if (!newVersion.equalsIgnoreCase(AntiLaby.getInstance().getDescription().getVersion())) {
						AntiLaby.LOG.info("Update found! Version " + newVersion + " is available.");
						updateAvailable = true;
					} else {
						AntiLaby.LOG.debug("No update found. You are running the newest version.");
						updateAvailable = false;
					}
				} else {
					AntiLaby.LOG.error("Failed to check for updates on spigotmc.org! (Invalid value received)");
					AntiLaby.LOG.error(newVersion);
					updateAvailable = false;
				}
			} else {
				AntiLaby.LOG.error("Failed to check for updates on spigotmc.org! (No information received)");
				updateAvailable = false;
			}
		} catch (final Exception ex) {
			AntiLaby.LOG.error("Failed to check for updates on spigotmc.org! (" + ex.getMessage() + ")");
			updateAvailable = false;
		}
		// Download and install update if available
		if (updateAvailable) {
			if (installUpdate("https://github.com/NathanNr/AntiLaby/releases/download/" + newVersion + "/AntiLaby.jar",
					1))
				AntiLaby.LOG.info("Auto-update complete! Reload or restart your server to activate the new version.");
			else
				AntiLaby.LOG.error(
						"Failed to install update from download server 1! Please install the newest version manually "
								+ "from" + " " + "https://www.spigotmc.org/resources/" + Constants.RESOURCE_ID + "/!");
			final File tmp = new File("plugins/AntiLaby.tmp");
			if (tmp.exists())
				tmp.delete();
		}
		AntiLaby.getInstance().disableIfNotCompatible();
		interrupt();
	}

	private boolean installUpdate(String urlString, int urlId) {
		AntiLaby.LOG.info("Downloading update from download server " + urlId + "...");
		try {
			final URL url = new URL(urlString);
			final URLConnection conn = url.openConnection();
			final InputStream is = new BufferedInputStream(conn.getInputStream());
			final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.tmp"));
			final byte[] chunk = new byte[1024];
			int chunkSize;
			while ((chunkSize = is.read(chunk)) != -1)
				os.write(chunk, 0, chunkSize);
			os.close();
			final File newfile = new File("plugins/AntiLaby.tmp");
			final long newlength = newfile.length();
			if (newlength <= 10000) {
				newfile.delete();
				return false;
			} else {
				AntiLaby.LOG.info("Installing update...");

				final FileInputStream is2 = new FileInputStream(new File("plugins/AntiLaby.tmp"));

				final OutputStream os2 = new BufferedOutputStream(
						new FileOutputStream(AntiLaby.getInstance().getFile()));
				final byte[] chunk2 = new byte[1024];
				int chunkSize2;
				while ((chunkSize2 = is2.read(chunk2)) != -1)
					os2.write(chunk2, 0, chunkSize2);
				is2.close();
				os2.close();

				final File tmp = new File("plugins/AntiLaby.tmp");
				tmp.delete();
				return true;
			}
		} catch (final FileNotFoundException e) {
			return false;
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}