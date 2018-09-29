package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static java.lang.Math.toIntExact;

/**
 * Checks for AntiLaby updates. This class loads, caches and parses the update information.
 *
 * @author NathanNr
 */
public class UpdateChecker {

	private static final Logger logger = new Logger("UpdateChecker");

	/**
	 * The location of the online update information provider
	 * TODO: Add the final URL, add alternate URL
	 */
	private final String uri = "http://localhost:8080/api/v1/com/github/antilaby/antilaby/update";

	private UpdateInformation updateInformation;

	public UpdateInformation getUpdateInformation() throws IOException, ParseException {
		return getUpdateInformation("release");
	}

	public UpdateInformation getUpdateInformation(String type) throws IOException, ParseException {
		if (updateInformation == null) {
			// Read from REST server
			logger.debug("Reading data from the REST server...");
			String raw = "";
			String url = uri;
			url += "?type=" + type;
			raw = new ReadJsonUrl(url).read();
			if (raw == null)
				return null;
			// JSON -> UpdateInformation
			logger.debug("Parsing raw data...");
			JSONObject jsonObject;
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(raw);
			jsonObject = (JSONObject) obj;
			updateInformation = new UpdateInformation((String) jsonObject.get("version"), toIntExact((long) jsonObject.get("versionId")), (String) jsonObject.get("downloadUrl"), (String) jsonObject.get("versionType"), toIntExact((long) jsonObject.get("updatePriority")), (String) jsonObject.get("sha256"), (String) jsonObject.get("updateNote"));
			logger.debug("Done!");
		}
		return updateInformation;
	}

	public void forceReload() {
		updateInformation = null;
		logger.debug("The reload of the data at the next request has been forced.");
	}

	@Deprecated
	private String getJson() throws Exception {
		return new ReadJsonUrl(uri).read();
	}

}
