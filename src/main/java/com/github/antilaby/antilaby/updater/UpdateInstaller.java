package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;

import java.io.*;

/**
 * Overwrite the old plug-in file with the new one
 *
 * @author NathanNr
 */
public class UpdateInstaller {

	private static final Logger LOGGER = new Logger("UpdateInstaller");

	/**
	 * Location of the temporary file
	 */
	private final String fileLocation;

	protected UpdateInstaller(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	/**
	 * Install the update: overwrite the old plug-in file with the new file
	 *
	 * @throws IOException
	 */
	public void install() throws IOException {
		LOGGER.debug("Overwriting old file...");
		final FileInputStream fileInputStream = new FileInputStream(new File(fileLocation));
		final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(AntiLaby.getInstance().getFile()));
		final byte[] chunk = new byte[1024];
		int chunkSize;
		while ((chunkSize = fileInputStream.read(chunk)) != -1)
			outputStream.write(chunk, 0, chunkSize);
		fileInputStream.close();
		outputStream.close();
		LOGGER.debug("Done!");
	}

}
