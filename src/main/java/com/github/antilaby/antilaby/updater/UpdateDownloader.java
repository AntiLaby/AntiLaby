package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;

import java.io.*;
import java.net.URL;

/**
 * Downloads the updated file
 *
 * @author NathanNr
 */
public class UpdateDownloader {

  private static final Logger LOGGER = new Logger("UpdateDownloader");

  private final UpdateInformation updateInformation;
  private final String temporaryFileLocation;

  protected UpdateDownloader(UpdateInformation updateInformation, String temporaryFileLocation) {
    this.updateInformation = updateInformation;
    this.temporaryFileLocation = temporaryFileLocation;
  }

  /**
   * Download the updated file
   *
   * @throws IOException
   */
  public void download() throws IOException {
    LOGGER.debug("Downloading update from download server...");
    final InputStream inputStream = new BufferedInputStream(new URL(updateInformation.getDownloadUrl()).openConnection().getInputStream());
    final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(temporaryFileLocation));
    //  IOUtils.copyStream(inputStream, outputStream);
    outputStream.close();
    final File tempFile = new File(temporaryFileLocation);
    final long newlength = tempFile.length();
    LOGGER.debug("Installing update...");
    final FileInputStream fileInputStream = new FileInputStream(tempFile);
    final OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(AntiLaby.getInstance().getFile()));
    //  IOUtils.copyStream(fileInputStream, outputStream1);
    fileInputStream.close();
    outputStream1.close();
    tempFile.delete();
    LOGGER.debug("Done!");
  }

  public String getTemporaryFileLocation() {
    return temporaryFileLocation;
  }

}
