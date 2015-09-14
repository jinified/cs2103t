import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TextBuddyTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private static final String MESSAGE_GENERIC = "\n%s\n\n";
  private static final String MESSAGE_EMPTY_ARGUMENT = "Empty Argument";
  private static final String MESSAGE_INVALID_COMMAND = "Invalid Command";
  private static final String MESSAGE_COMMAND = "command: ";
  private static final String COMMAND_ADD = "add";
  private static final String COMMAND_DELETE = "delete";
  private static final String COMMAND_CLEAR = "clear";
  private static final String COMMAND_DISPLAY = "display";
  private static final String COMMAND_EXIT = "exit";

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void cleanUpStreams() {
    System.setOut(null);
    System.setErr(null);
  }

  @Test
  public void testInvalidCommand() throws IOException {
    TextBuddy.processCommand("kill me");
    assertEquals(String.format(MESSAGE_GENERIC, MESSAGE_INVALID_COMMAND), 
        outContent.toString());
  }

}
