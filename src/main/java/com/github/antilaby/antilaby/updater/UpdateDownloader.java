package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Downloads the updated file
 *
 * @author NathanNr
 */
public class UpdateDownloader {

  private static final Logger LOGGER = new Logger("UpdateDownloader");

  private final UpdateInformation info;
  private final Path temporaryFileLocation;

  protected UpdateDownloader(UpdateInformation info, Path temporaryFileLocation) {
    this.info = info;
    this.temporaryFileLocation = temporaryFileLocation;
  }

  /**
   * Download the new file.
   *
   * @throws IOException if the transfer fails
   */
  public void download() throws IOException {
    LOGGER.debug("Downloading update from download server...");
    if (Files.exists(temporaryFileLocation)) {
      Files.delete(temporaryFileLocation);
    }
    Files.createFile(temporaryFileLocation);
    ReadableByteChannel rbc = Channels.newChannel(new URL(info.downloadUrl).openStream());
    FileChannel fc = FileChannel.open(temporaryFileLocation, StandardOpenOption.WRITE);
    fc.transferFrom(rbc, 0, Long.MAX_VALUE);
    rbc.close();
    fc.close();
    LOGGER.debug("Done!");
  }

  Path getTemporaryFileLocation() {
    return temporaryFileLocation;
  }

}
