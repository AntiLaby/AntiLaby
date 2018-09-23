package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadJsonUrl {

	private final Logger logger = new Logger("ReadJsonUrl");

	private final String uri;

	public ReadJsonUrl(String uri) {
		this.uri = uri;
	}

	public String read() throws Exception {
		URL url;
		HttpURLConnection httpURLConnection;
		String raw = "";
		url = new URL(uri);
		httpURLConnection =
				(HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		httpURLConnection.setRequestProperty("Accept", "application/json");
		if (httpURLConnection.getResponseCode() != 200) {
			throw new Exception("Failed read data, got HTTP status code '" + httpURLConnection.getResponseCode() + "' from the web server.");
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
		String output;
		while ((output = bufferedReader.readLine()) != null) {
			raw += output;
		}
		httpURLConnection.disconnect();
		return raw;
	}

}
