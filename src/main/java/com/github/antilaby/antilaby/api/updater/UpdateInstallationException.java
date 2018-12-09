package com.github.antilaby.antilaby.api.updater;

/**
 * Exception while installing an update
 *
 * @author NathanNr
 */

public class UpdateInstallationException extends Exception {

  private static final long serialVersionUID = -8663702121048060809L;

  public UpdateInstallationException(String location, String message) {
    super("[AntiLaby/Updater/" + location + "] " + message);
  }

}
