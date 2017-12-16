package de.heisluft.antilaby.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;

/**
 * This class defines utility methods for file handling
 *
 * @author heisluft
 *
 */
public final class Files {

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
	 * Private constructor, no need to instantiate this class
	 */
	private Files() {}
}
