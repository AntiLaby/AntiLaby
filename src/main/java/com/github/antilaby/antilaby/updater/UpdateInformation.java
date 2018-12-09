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

  /**
   * Version number of the update
   *
   * @return version
   */
  public String getVersion() {
    return version;
  }

  /**
   * Build-ID of the update
   *
   * @return Build-ID
   */
  public int getVersionId() {
    return versionId;
  }

  /**
   * URL where the file should be downloaded
   *
   * @return url
   */
  public String getDownloadUrl() {
    return downloadUrl;
  }

  /**
   * VersionType of the update (e.g. release, beta)
   *
   * @return VersionType
   */
  public String getVersionType() {
    return versionType;
  }

  /**
   * Priority of the update
   *
   * @return priority
   */
  public int getUpdatePriority() {
    return updatePriority;
  }

  /**
   * SHA-256 value of the file
   *
   * @return sha-256
   */
  public String getSha256() {
    return sha256;
  }

  /**
   * Short message about the update
   *
   * @return message
   */
  public String getUpdateNote() {
    return updateNote;
  }

}
