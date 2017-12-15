package de.heisluft.antilaby.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

public final class Files {
	
	public static void copyStream(InputStream from, OutputStream to) throws IOException {
		int next = 0;
		while ((next = from.read()) != -1)
			to.write(next);
	}

	@SuppressWarnings("unchecked")
	public static <T> T readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		final Object o = in.readObject();
		System.out.println(o.getClass());
		return (T) in.readObject();
	}
	
	private Files() {}
}
