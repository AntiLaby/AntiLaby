package com.github.antilaby.antilaby.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.antilaby.DummyServer;
import com.github.antilaby.antilaby.api.command.ExecutableCommand;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author NathanNr
 */
public class ExecutableCommandTest {

  @BeforeAll
  public static void setup() throws ReflectiveOperationException {
    Field f = Bukkit.class.getDeclaredField("server");
    f.setAccessible(true);
    f.set(null, new DummyServer());
  }

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
    String commandLine = "# /gamemode 0 @a";

    //when
    ExecutableCommand executableCommand = new ExecutableCommand(commandLine);

    //then
    assertEquals(null, executableCommand.getCommandLine());
  }

}
