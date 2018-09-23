package com.github.antilaby.antilaby.updater;

/**
 * Update information for AntiLaby
 *
 * @author NathanNr
 */
public class UpdateInformation {

	private final String version;
	private final int versionId;
	private final String downloadUrl;
	private final String versionType;
	private final int updatePriority;
	private final String sha256;
	private final String updateNote;

	public UpdateInformation(String version, int versionId, String downloadUrl, String versionType, int updatePriority, String sha256, String updateNote) {
		this.version = version;
		this.versionId = versionId;
		this.downloadUrl = downloadUrl;
		this.versionType = versionType;
		this.updatePriority = updatePriority;
		this.sha256 = sha256;
		this.updateNote = updateNote;
	}

	public String getVersion() {
		return version;
	}

	public int getVersionId() {
		return versionId;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public String getVersionType() {
		return versionType;
	}

	public int getUpdatePriority() {
		return updatePriority;
	}

	public String getSha256() {
		return sha256;
	}

	public String getUpdateNote() {
		return updateNote;
	}

}
