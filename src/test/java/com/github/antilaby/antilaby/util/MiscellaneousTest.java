package com.github.antilaby.antilaby.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author NathanNr
 */
public class MiscellaneousTest {

  @Test
  public void assertThatBoolToIntConvertsProperly() {
    assertEquals(1, Miscellaneous.boolToInt(true));
    assertEquals(0, Miscellaneous.boolToInt(false));
  }

}
