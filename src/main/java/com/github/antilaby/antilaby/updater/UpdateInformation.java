package com.github.antilaby.antilaby.updater;

/**
 * Update information for AntiLaby
 *
 * @author NathanNr
 */
public class UpdateInformation {

  final String version;
  final int versionId;
  final String downloadUrl;
  final String versionType;
  final int updatePriority;
  final String sha256;
  final String updateNote;

  public UpdateInformation(String version, int versionId, String downloadUrl, String versionType, int updatePriority, String sha256, String updateNote) {
    this.version = version;
    this.versionId = versionId;
    this.downloadUrl = downloadUrl;
    this.versionType = versionType;
    this.updatePriority = updatePriority;
    this.sha256 = sha256;
    this.updateNote = updateNote;
  }
}
