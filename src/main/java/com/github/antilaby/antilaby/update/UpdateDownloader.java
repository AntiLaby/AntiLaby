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
import java.net.URLConnection;

/**
 * The old auto-updater class. It will be removed as soon as the new
 * auto-updater is ready for use.
 *
 * @author NathanNr
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class UpdateDownloader extends Thread {

	/**
	 * checks for updates and initiates their download and installation if activated
	 */
	@Override
	public void run() {
		AntiLaby.LOG.debug("Checking for updates on spigotmc.org...");
		String newVersion;
		try {
			newVersion = IOUtils.readUrl("https://api.spigotmc.org/legacy/update.php?resource=" + Constants.RESOURCE_ID);
			if(!newVersion.isEmpty()) {
				if(!newVersion.contains(" ") || !newVersion.contains("!")) {
					if(!newVersion.equalsIgnoreCase(AntiLaby.getInstance().getDescription().getVersion())) {
						AntiLaby.LOG.info("Update found! Version " + newVersion + " is available.");
					} else {
						AntiLaby.LOG.debug("No update found. You are running the newest version.");
						return;
					}
				} else {
					AntiLaby.LOG.error("Failed to check for updates on spigotmc.org! (Invalid value received: " + newVersion + ")");
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
		else
			AntiLaby.LOG.error("Failed to install update! Please install the newest version manually from " + Constants.RESOURCE_LINK + "/!");
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
			final URL url = new URL(urlString);
			final URLConnection conn = url.openConnection();
			final InputStream is = new BufferedInputStream(conn.getInputStream());
			final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.tmp"));
			final byte[] chunk = new byte[1024];
			int chunkSize;
			while((chunkSize = is.read(chunk)) != -1) os.write(chunk, 0, chunkSize);
			os.close();
			final File newfile = new File("plugins/AntiLaby.tmp");
			final long newlength = newfile.length();
			if(newlength <= 10000) {
				newfile.delete();
				return false;
			} else {
				AntiLaby.LOG.info("Installing update...");
				final FileInputStream is2 = new FileInputStream(newfile);
				final OutputStream os2 = new BufferedOutputStream(new FileOutputStream(AntiLaby.getInstance().getFile()));
				final byte[] chunk2 = new byte[1024];
				int chunkSize2;
				while((chunkSize2 = is2.read(chunk2)) != -1) os2.write(chunk2, 0, chunkSize2);
				is2.close();
				os2.close();
				newfile.delete();
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