package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import static java.lang.Math.toIntExact;

/**
 * Checks for AntiLaby updates. This class loads, caches and parses the update information.
 * *
 *
 * @author NathanNr
 */
public class UpdateChecker {

	/**
	 * The location of the online update information provider
	 * TODO: Add the final URL
	 */
	private final String uri = "http://localhost:8080/api/v1/com/github/antilaby/antilaby/update";
	private final Logger logger = new Logger("UpdateChecker");

	public UpdateInformation getUpdateInformation() {
		return getUpdateInformation("release");
	}

	public UpdateInformation getUpdateInformation(String type) {
		UpdateInformation updateInformation = null;
		// Read from REST server
		String raw = "";
		try {
			String url = uri;
			url += "?type=" + type;
			raw = new ReadJsonUrl(url).read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (raw == null)
			return null;
		// JSON -> UpdateInformation
		JSONObject jsonObject;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(raw);
			jsonObject = (JSONObject) obj;
			updateInformation = new UpdateInformation((String) jsonObject.get("version"), toIntExact((long) jsonObject.get("versionId")), (String) jsonObject.get("downloadUrl"), (String) jsonObject.get("versionType"), toIntExact((long) jsonObject.get("updatePriority")), (String) jsonObject.get("sha256"), (String) jsonObject.get("updateNote"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updateInformation;
	}

	@Deprecated
	private String getJson() throws Exception {
		return new ReadJsonUrl(uri).read();
	}

}
