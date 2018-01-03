package com.github.antilaby.antilaby.api.updater;

import java.net.URL;

public class UpdateInstaller {

	private AvailableUpdates updateInformation;
	private URL url;

	public UpdateInstaller(AvailableUpdates updateInformation) {
		this.updateInformation = updateInformation;
	}

	public UpdateInstaller(URL url) {
		this.url = url;
	}

	public AvailableUpdates getUpdateInformation() {
		return this.updateInformation;
	}

	public URL getURL() {
		return this.url;
	}

}
