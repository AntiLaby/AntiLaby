package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;

public class DataManager {

  private static final Path PATH = AntiLaby.getInstance().getDataPath().resolve("/labyinfo.ser");

  /**
   * Private constructor, no need to instantiate this class.
   */
  private DataManager() {
    throw new UnsupportedOperationException();
  }

  /**
   * Reads the next object from the given {@link ObjectStreamException} as an instance of T.
   *
   * @param <T>         The class to cast the read object to
   * @param inputStream The stream to read from
   * @return The read object, casted to T
   * @throws ClassNotFoundException Class of a serialized object cannot be found
   * @throws IOException            Any of the usual Input/Output related exceptions
   * @throws ClassCastException     The read object is not an instance of T
   */
  @SuppressWarnings("unchecked")
  private static <T> T readObject(ObjectInputStream inputStream) throws ClassNotFoundException,
      IOException {
    return (T) inputStream.readObject();
  }

  /**
   * Loads the data file.
   */
  public static void loadData() {
    if (Files.isRegularFile(PATH) && Bukkit.getOnlinePlayers().size() != 0) {
      ObjectInputStream ois = null;
      try {
        ois = new ObjectInputStream(Files.newInputStream(PATH));
        final HashMap<String, String> p = readObject(ois);
        for (final Entry<String, String> e : p.entrySet()) {
          System.out.println(e);
        }
        IncomingPluginChannel.setLabyModPlayers(p);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        closeSilently(ois);
      }
      cleanup();
    }
  }

  /**
   * Deletes the data file.
   */
  public static void cleanup() {
    if (Files.exists(PATH)) {
      try {
        Files.delete(PATH);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Closes the given {@link Closeable}, ignoring a possibly thrown Exception.
   *
   * @param toClose the Closable to close
   */
  private static void closeSilently(@Nullable Closeable toClose) {
    if (toClose != null) {
      try {
        toClose.close();
      } catch (IOException e) {
        // ignore
      }
    }
  }

  /**
   * Saves the data file.
   */
  public static void saveData() {
    if (IncomingPluginChannel.getLabyModPlayers().isEmpty()) {
      return;
    }
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(Files.newOutputStream(PATH));
      oos.writeObject(IncomingPluginChannel.getLabyModPlayers());
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      closeSilently(oos);
    }
  }
}
