package com.github.antilaby.antilaby.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.net.URL;

/**
 * This class defines utility methods for I/O handling
 *
 * @author heisluft
 */
public final class IOUtils {

	/**
	 * Passes all read bytes from the given {@link InputStream} to the given
	 * {@link OutputStream}
	 *
	 * @param readFrom
	 *            The InputStream to read from
	 * @param writeTo
	 *            The OutputStream to write to
	 * @throws IOException
	 *             If an IO error occurs during reading or writing
	 */
	public static void copyStream(InputStream readFrom, OutputStream writeTo) throws IOException {
		int nextByte = 0;
		while ((nextByte = readFrom.read()) != -1)
			writeTo.write(nextByte);
	}
	
	/**
	 * Reads the next object from the given {@link ObjectStreamException} and casts
	 * it to T
	 *
	 * @param <T>
	 *            The class to cast the read object to
	 * @param inputStream
	 *            The stream to read from
	 * @return The read object, casted to T
	 * @throws ClassNotFoundException
	 *             Class of a serialized object cannot be found
	 * @throws IOException
	 *             Any of the usual Input/Output related exceptions
	 * @throws ClassCastException
	 *             The read object is not an instance of T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(ObjectInputStream inputStream)
			throws ClassNotFoundException, IOException, ClassCastException {
		return (T) inputStream.readObject();
	}

	/**
	 * Gets the text from a web page
	 *
	 * @param urlString
	 *            The URL to be read
	 * @return The text
	 * @throws IOException
	 *             If an IO Error occurred
	 */
	public static String readUrl(String urlString) throws IOException {
		BufferedReader reader = null;
		try {
			final URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			final StringBuffer buffer = new StringBuffer();
			int read;
			final char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			return buffer.toString();
		} finally {
			if (reader != null) reader.close();
		}
	}
	
	/**
	 * Private constructor, no need to instantiate this class
	 */
	private IOUtils() {}
	
}
