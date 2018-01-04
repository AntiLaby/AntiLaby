package com.github.antilaby.antilaby.api.updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.HashMap;

import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;

/**
 * Installs an update. It is highly recommended to run this task in another
 * thread and not in the main thread!
 * 
 * @author NathanNr
 */

public class UpdateInstaller {

	private final Logger LOG = new Logger("UpdateInstaller");

	private Update update;

	private final String tmpUpdateFilePath = "plugins/AntiLaby.tmp";

	/**
	 * @param update
	 *            The update to install
	 */
	public UpdateInstaller(Update update) {
		this.update = update;
	}

	/**
	 * @return The update to install
	 */
	public Update getUpdate() {
		return this.update;
	}

	/**
	 * Installs the provided update
	 * 
	 * @throws UpdateInstallationException
	 *             If the update installation fails.
	 */
	public void install() throws UpdateInstallationException {
		// Download and save the update file
		LOG.info("Downloading update to version " + update.getUpdateVersion() + "...");
		if (update.getUpdateShortMessage() != null)
			LOG.info("Update message: " + update.getUpdateShortMessage());
		else
			LOG.debug("No update message has been provided.");
		LOG.debug("Update download URL: " + update.getUpdateUrl());
		try {
			final URLConnection conn = update.getUpdateUrl().openConnection();
			final InputStream is = new BufferedInputStream(conn.getInputStream());
			final OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpUpdateFilePath));
			final byte[] chunk = new byte[1024];
			int chunkSize;
			while ((chunkSize = is.read(chunk)) != -1)
				os.write(chunk, 0, chunkSize);
			os.close();
		} catch (IOException e) {
			throw new UpdateInstallationException("Download", "Failed to download or save the update file!");
		}
		LOG.info("Download finished! Installing update...");
		// Validate update
		validateUpdate(new File(tmpUpdateFilePath));
		// Install the update: overwrite the old plug-in file with the new file
		try {
			final FileInputStream is = new FileInputStream(new File(tmpUpdateFilePath));
			final OutputStream os = new BufferedOutputStream(new FileOutputStream(AntiLaby.getInstance().getFile()));
			final byte[] chunk = new byte[1024];
			int chunkSize;
			while ((chunkSize = is.read(chunk)) != -1)
				os.write(chunk, 0, chunkSize);
			is.close();
			os.close();
		} catch (IOException e) {
			throw new UpdateInstallationException("Installation",
					"Failed to overwrite the old plug-in file with the new one!");
		} finally {
			final File tmp = new File("plugins/AntiLaby.tmp");
			if (tmp.exists()) {
				tmp.delete();
			}
		}
		LOG.info("The update to version " + update.getUpdateVersion()
				+ " has been finished. Please reload or restart you server to enable the new version!");
	}

	/**
	 * Validates an update if it is correct
	 * 
	 * @param updateFile
	 *            The file of the update
	 * @return Returns true, if the update is okay
	 * @throws UpdateInstallationException
	 *             If the update file does not match the provided requirements.
	 */
	private void validateUpdate(File tmpUpdateFile) throws UpdateInstallationException {
		// TODO: Better validation of the update
		HashMap<String, String> validationInfos = update.getValidationInfos();
		if (validationInfos != null) {
			if (validationInfos.containsKey("minsize") && validationInfos.containsKey("maxsize")) {
				try {
					final long updateLength = tmpUpdateFile.length();
					if (updateLength < Integer.valueOf(validationInfos.get("minsize"))) {
						tmpUpdateFile.delete();
						throw new UpdateInstallationException("Validation",
								"The downloaded update file does not match the provided requirements! Please contact the plug-in administrator.");
					}
					if (updateLength > Integer.valueOf(validationInfos.get("maxsize"))) {
						tmpUpdateFile.delete();
						throw new UpdateInstallationException("Validation",
								"The downloaded update file does not match the provided requirements! Please contact the plug-in administrator.");
					}
				} catch (NumberFormatException e) {
					// Do nothing
				}
			}
		}
	}

}
