package com.github.antilaby.antilaby.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class defines utility methods for I/O handling
 *
 * @author heisluft
 */
public final class IOUtils {

  /**
   * Private constructor, no need to instantiate this class
   */
  private IOUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Passes all read bytes from the given {@link InputStream} to the given {@link Path}
   *
   * @param readFrom The InputStream to read from
   * @param writeTo  The Path to write to
   * @throws IOException If an IO error occurs during reading or writing
   */
  public static void copyStream(InputStream readFrom, Path writeTo) throws IOException {
    ByteBuf bb = Unpooled.buffer();
    int nextByte;
    while ((nextByte = readFrom.read()) != -1) {
      bb.writeByte(nextByte);
    }
    Files.write(writeTo, bb.array());
  }

  /**
   * Closes the given {@link Closeable}, ignoring thrown Exception
   *
   * @param toClose the Closable to close
   */
  public static void closeSilently(@Nullable Closeable toClose) {
    if (toClose == null) {
      return;
    }
    try {
      toClose.close();
    } catch (IOException e) {
      // ignore
    }
  }

  /**
   * Reads the next object from the given {@link ObjectStreamException} and casts it to T
   *
   * @param <T>         The class to cast the read object to
   * @param inputStream The stream to read from
   * @return The read object, casted to T
   * @throws ClassNotFoundException Class of a serialized object cannot be found
   * @throws IOException            Any of the usual Input/Output related exceptions
   * @throws ClassCastException     The read object is not an instance of T
   */
  @SuppressWarnings("unchecked")
  @Nonnull
  public static <T> T readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
    return (T) inputStream.readObject();
  }

  /**
   * Gets the text from a web page
   *
   * @param urlString The URL to be read
   * @return The text
   * @throws IOException If an IO Error occurred
   */
  @Nonnull
  public static String readUrl(String urlString) throws IOException {
    BufferedReader reader = null;
    try {
      final URL url = new URL(urlString);
      reader = new BufferedReader(new InputStreamReader(url.openStream()));
      final StringBuilder builder = new StringBuilder();
      int read;
      final char[] chars = new char[1024];
      while ((read = reader.read(chars)) != -1) {
        builder.append(chars, 0, read);
      }
      return builder.toString();
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

}
