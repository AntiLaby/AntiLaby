package com.github.antilaby.antilaby.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author NathanNr
 */
public class MiscellaneousTest {

  @Test
  public void assertThatBoolToIntConvertsProperly() {
    //given
    boolean booleanTrue = true;
    boolean booleanFalse = false;

    //when
    int intTrue = 1;
    int intFalse = 0;

    //then
    assertEquals(intTrue, Miscellaneous.boolToInt(booleanTrue));
    assertEquals(intFalse, Miscellaneous.boolToInt(booleanFalse));
  }

}
