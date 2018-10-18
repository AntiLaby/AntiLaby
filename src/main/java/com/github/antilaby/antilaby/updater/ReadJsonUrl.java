package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Read JSON from a HTTP server
 *
 * @author NathanNr
 */
public class ReadJsonUrl {

	private static final Logger LOGGER = new Logger("ReadJsonUrl");

	private final String uri;

	public ReadJsonUrl(String uri) {
		this.uri = uri;
	}

	/**
	 * Read JSON from a HTTP server
	 * @return JSON as String
	 * @throws IOException Error while reading JSON
	 */
	public String read() throws IOException {
		URL url;
		HttpURLConnection httpURLConnection;
		String raw = "";
		url = new URL(uri);
		LOGGER.debug("Connecting to '" + url.toString() + "'...");
		httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setRequestProperty("Accept", "application/json");
		if (httpURLConnection.getResponseCode() != 200)
			throw new IOException("Failed read data, got HTTP status code '" + httpURLConnection.getResponseCode() + "' from the web server.");
		LOGGER.debug("Reading input stream...");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
		String output;
		while ((output = bufferedReader.readLine()) != null)
			raw += output;
		httpURLConnection.disconnect();
		LOGGER.debug("Done!");
		return raw;
	}

}
