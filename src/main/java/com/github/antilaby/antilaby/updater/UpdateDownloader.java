package com.github.antilaby.antilaby.updater;

import java.io.IOException;

public class UpdateDownloader {

	private final UpdateInformation updateInformation;
	private String fileLocation;

	protected UpdateDownloader(UpdateInformation updateInformation) {
		this.updateInformation = updateInformation;
	}

	public void download() throws IOException {
		// TODO
	}

	public String getFileLocation() {
		return fileLocation;
	}

}
