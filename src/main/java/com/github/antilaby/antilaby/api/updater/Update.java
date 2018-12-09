package com.github.antilaby.antilaby.api.updater;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Provides information of an available update
 *
 * @author NathanNr
 */

public class Update {

  private JavaPlugin plugin;
  private VersionType updateVersionType;
  private URL updateUrl;
  private String updateVersion;
  private String updateShortMessage;
  private String updateChangelog;
  private boolean updateApiChanges;
  private boolean updateRequiresConfigUpdate;
  private boolean updateConfigUpdateAuto;
  private UpdatePriority updatePriority = UpdatePriority.UNKNOWN;
  private HashMap<String, String> validationInfos;
  private ArrayList<UpdateRawMessage> updateRawMessages;

  /**
   * @param plugin
   *     The JavaPlugin
   * @param updateVersion
   *     The version number of the update
   * @param updateUrl
   *     The update download URL
   */
  public Update(JavaPlugin plugin, String updateVersion, URL updateUrl) {
    this.plugin = plugin;
    this.updateVersion = updateVersion;
    this.updateUrl = updateUrl;
  }

  /**
   * @return The JavaPlugin
   */
  public JavaPlugin getPlugin() {
    return plugin;
  }

  /**
   * @return The version type of the update
   */
  public VersionType getUpdateVersionType() {
    return updateVersionType;
  }

  /**
   * @param updateVersionType
   *     Set the version type of the update
   */
  public void setUpdateVersionType(VersionType updateVersionType) {
    this.updateVersionType = updateVersionType;
  }

  /**
   * @return The version number of the update
   */
  public String getUpdateVersion() {
    return updateVersion;
  }

  /**
   * @param updateVersion
   *     Set the version number of the update
   */
  public void setUpdateVersion(String updateVersion) {
    this.updateVersion = updateVersion;
  }

  /**
   * @return The update download URL
   */
  public URL getUpdateUrl() {
    return this.updateUrl;
  }

  /**
   * @param updateUrl
   *     Set the update download URL
   */
  public void setUpdateUrl(URL updateUrl) {
    this.updateUrl = updateUrl;
  }

  /**
   * @return A short update message
   */
  public String getUpdateShortMessage() {
    return updateShortMessage;
  }

  /**
   * @param updateShortMessage
   *     A short update message
   */
  public void setUpdateShortMessage(String updateShortMessage) {
    this.updateShortMessage = updateShortMessage;
  }

  /**
   * @return The full update change log
   */
  public String getUpdateChangelog() {
    return updateChangelog;
  }

  /**
   * @param updateChangelog
   *     Set the full update change log
   */
  public void setUpdateChangelog(String updateChangelog) {
    this.updateChangelog = updateChangelog;
  }

  /**
   * @return Indicates if the update includes changes to the AntiLaby API
   */
  public boolean isUpdateApiChanges() {
    return updateApiChanges;
  }

  /**
   * @param updateApiChanges
   *     Set if the update includes changes to the AntiLaby API
   */
  public void setUpdateApiChanges(boolean updateApiChanges) {
    this.updateApiChanges = updateApiChanges;
  }

  /**
   * @return Indicates if the update requires a configuration update
   */
  public boolean isUpdateRequiresConfigUpdate() {
    return updateRequiresConfigUpdate;
  }

  /**
   * @param updateRequiresConfigUpdate
   *     Set if the update requires a configuration update
   */
  public void setUpdateRequiresConfigUpdate(boolean updateRequiresConfigUpdate) {
    this.updateRequiresConfigUpdate = updateRequiresConfigUpdate;
  }

  /**
   * @return Indicates if the configuration file will be converted automatically to the new design
   */
  public boolean isUpdateConfigUpdateAuto() {
    return updateConfigUpdateAuto;
  }

  /**
   * @param updateConfigUpdateAuto
   *     Set if the configuration file will be converted automatically to the new design
   */
  public void setUpdateConfigUpdateAuto(boolean updateConfigUpdateAuto) {
    this.updateConfigUpdateAuto = updateConfigUpdateAuto;
  }

  /**
   * @return The priority of the update
   */
  public UpdatePriority getUpdatePriority() {
    return updatePriority;
  }

  /**
   * @param updatePriority
   *     Set the priority of the update
   */
  public void setUpdatePriority(UpdatePriority updatePriority) {
    this.updatePriority = updatePriority;
  }

  /**
   * @return The information to check, if the file has been downloaded correctly
   */
  public HashMap<String, String> getValidationInfos() {
    return validationInfos;
  }

  /**
   * @param validationInfos
   *     Set the information to check, if the file has been downloaded correctly
   */
  public void setValidationInfos(HashMap<String, String> validationInfos) {
    this.validationInfos = validationInfos;
  }

  /**
   * @return The update raw messages
   */
  public ArrayList<UpdateRawMessage> getUpdateRawMessages() {
    return updateRawMessages;
  }

  /**
   * @param updateRawMessages
   *     Set the information to check, if the file has been downloaded correctly
   */
  public void setUpdateRawMessages(ArrayList<UpdateRawMessage> updateRawMessages) {
    this.updateRawMessages = updateRawMessages;
  }

}
