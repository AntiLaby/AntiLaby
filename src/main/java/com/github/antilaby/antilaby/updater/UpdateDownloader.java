package com.github.antilaby.antilaby.updater;

import java.io.IOException;

public class UpdateDownloader {

	private final UpdateInformation updateInformation;
	private final String temporaryFileLocation;

	protected UpdateDownloader(UpdateInformation updateInformation, String temporaryFileLocation) {
		this.updateInformation = updateInformation;
		this.temporaryFileLocation = temporaryFileLocation;
	}

	public void download() throws IOException {

		// TODO
	}

	public String getTemporaryFileLocation() {
		return temporaryFileLocation;
	}

}
