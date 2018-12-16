package com.github.antilaby.antilaby.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.antilaby.antilaby.api.command.ExecutableCommand;
import org.junit.jupiter.api.Test;

/**
 * @author NathanNr
 */
public class ExecutableCommandTest {

  @Test
  public void assertThatTheCommandLineWontBeChangedIfItIsAlreadyFormattedProperly() {
    //given
    String commandLine = "gamemode 0 @a";

    //when
    ExecutableCommand executableCommand = new ExecutableCommand(commandLine);

    //then
    assertEquals("gamemode 0 @a", executableCommand.getCommandLine());
  }

  @Test
  public void assertThatThePrecedingSlashOfTheCommandLineWillBeRemovedWhileFormattingIt() {
    //given
    String commandLine = "/gamemode 0 @a";

    //when
    ExecutableCommand executableCommand = new ExecutableCommand(commandLine);

    //then
    assertEquals("gamemode 0 @a", executableCommand.getCommandLine());
  }

  @Test
  public void assertThatTheCommandLineWillBeDiscardedIfItHasAPrecedingNumberSign() {
    //given
    String commandLine = "# gamemode 0 @a";

    //when
    ExecutableCommand executableCommand = new ExecutableCommand(commandLine);

    //then
    assertEquals(null, executableCommand.getCommandLine());
  }

}
