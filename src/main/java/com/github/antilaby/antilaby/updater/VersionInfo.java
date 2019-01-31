package com.github.antilaby.antilaby.updater;

import com.github.zafarkhaja.semver.Version;
import org.json.simple.JSONObject;

/**
 * Version information for AntiLaby
 *
 * @author heisluft
 */
public class VersionInfo {

  final Version version;
  final String downloadUrl, md5, sha1;

  VersionInfo(String version, String downloadUrl, String sha1, String md5) {
    this.version = Version.valueOf(version);
    this.downloadUrl = downloadUrl;
    this.sha1 = sha1;
    this.md5 = md5;
  }

  static VersionInfo of(JSONObject object) {
    return new VersionInfo((String) object.get("version"), (String) object.get("downloadUrl"),
        (String) object.get("sha1"), (String) object.get("md5"));
  }
}
