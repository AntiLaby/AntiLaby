package com.github.antilaby.antilaby.api.exceptions;

import com.github.antilaby.antilaby.util.Constants;

/**
 * Exception for problems with AntiLaby.
 *
 * @author NathanNr
 */
public class InternalException extends RuntimeException {

  private static final long serialVersionUID = 4682945371734435384L;

  public InternalException(String location, String message, String id) {
    super(id == null
        ? "[AntiLaby/" + location + "] An internal error occured: \"" + message + "\" Please report " + "the bug with the full stack trace here: " + Constants.BUG_REPORT_URL :
        "[AntiLaby/" + location + "] An internal error occured" + ". " + "\"" + message + "\" Please report the bug with the id '" + id + "' and the full stack trace here: "
            + Constants.BUG_REPORT_URL);
  }

}
