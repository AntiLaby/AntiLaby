package com.github.antilaby.antilaby.api.updater;

public class Update {

	private VersionType updateVersionType;
	private String updateVersion;
	private String updateShortMessage;
	private String updateChangelog;
	private boolean updateApiChanges;
	private boolean updateRequiresConfigUpdate;
	private boolean updateConfigUpdateAuto;
	private UpdatePriority updatePriority = UpdatePriority.UNKNOWN;

	public Update(VersionType updateVersionType, String updateVersion) {
		this.updateVersionType = updateVersionType;
		this.updateVersion = updateVersion;
	}

}
