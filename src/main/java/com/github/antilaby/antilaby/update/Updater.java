package com.github.antilaby.antilaby.update;

import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import com.github.antilaby.antilaby.util.IOUtils;

import javax.annotation.Nonnull;
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

/**
 * The old auto-updater class. It will be removed as soon as the new auto-updater is ready for use.
 *
 * @author NathanNr
 */
@Deprecated
public class Updater extends Thread {

	/**
	 * checks for updates and initiates their download and installation if activated
	 */
	@Override
	public void run() {
		AntiLaby.LOG.debug("Checking for updates on spigotmc.org...");
		String newVersion;
		try {
			newVersion = IOUtils.readUrl(Constants.UPDATE_URL);
			if(!newVersion.isEmpty()) {
				if(!newVersion.contains(" ") || !newVersion.contains("!")) {
					if(!newVersion.equalsIgnoreCase(AntiLaby.getInstance().getDescription().getVersion())) {
						AntiLaby.LOG.info("Update found! Version " + newVersion + " is available.");
					} else {
						AntiLaby.LOG.debug("No update found. You are running the newest version.");
						return;
					}
				} else {
					AntiLaby.LOG.error(
							"Failed to check for updates on spigotmc.org! (Invalid value received: " + newVersion +
									")");
					return;
				}
			} else {
				AntiLaby.LOG.error("Failed to check for updates on spigotmc.org! (No information received)");
				return;
			}
		} catch(final Exception ex) {
			AntiLaby.LOG.error("Failed to check for updates on spigotmc.org! (" + ex.getMessage() + ")");
			return;
		}
		// Download and install update if available
		if(installUpdate("https://github.com/NathanNr/AntiLaby/releases/download/" + newVersion + "/AntiLaby.jar"))
			AntiLaby.LOG.info("Auto-update complete! Reload or restart your server to activate the new version.");
		else AntiLaby.LOG.error(
				"Failed to install update! Please install the newest version manually from " + Constants.RESOURCE_LINK
						+ "!");
		final File tmp = new File("plugins/AntiLaby.tmp");
		if(tmp.exists()) tmp.delete();
		AntiLaby.getInstance().disableIfNotCompatible();
		interrupt();
	}

	/**
	 * Tries to download and install the update if enabled in the config
	 *
	 * @param urlString
	 * 		the Non-null update URL
	 *
	 * @return true if the update task completed, false otherwise
	 */
	private boolean installUpdate(@Nonnull String urlString) {
		AntiLaby.LOG.info("Downloading update from download server...");
		try {
			final InputStream is = new BufferedInputStream(new URL(urlString).openConnection().getInputStream());
			final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.tmp"));
			IOUtils.copyStream(is, os);
			os.close();
			final File tempFile = new File("plugins/AntiLaby.tmp");
			final long newlength = tempFile.length();
			if(newlength < 10000) {
				tempFile.delete();
				return false;
			} else {
				AntiLaby.LOG.info("Installing update...");
				final FileInputStream is2 = new FileInputStream(tempFile);
				final OutputStream os2 = new BufferedOutputStream(
						new FileOutputStream(AntiLaby.getInstance().getFile()));
				IOUtils.copyStream(is2, os2);
				is2.close();
				os2.close();
				tempFile.delete();
				return true;
			}
		} catch(final FileNotFoundException e) {
			return false;
		} catch(final IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}