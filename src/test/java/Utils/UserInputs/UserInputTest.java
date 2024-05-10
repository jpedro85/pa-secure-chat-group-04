package Utils.UserInputs;

import Utils.UserInputs.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class UserInputTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private void mockInput( String input )
    {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    @DisplayName("test command execution.")
    public void testExecuteFromInput() {

        mockInput("sayName Alberto\nsayHello\n");

        UserInput userInput = new UserInput();
        userInput.addCommand(new Command("sayHello", (args) -> System.out.println("Hello!")));
        userInput.addCommand(new Command("sayName", (args) -> System.out.println("Your name is: " + args)));

        userInput.executeFromInput();
        userInput.executeFromInput();

        assertTrue( outContent.toString().contains("Your name is: Alberto") );
        assertTrue( outContent.toString().contains("Hello!") );
    }

    @Test
    @DisplayName("Test Add and Remove.")
    public void testAddRemoveCommand()
    {
        mockInput("sayName Alberto\nsayHello\n");

        UserInput userInput = new UserInput();
        Command sayHelloCommand = new Command("sayHello", (args) -> System.out.println("Hello!"));
        Command sayNameCommand = new Command("sayName", (args) -> System.out.println("Your name is: " + args));
        userInput.addCommand(sayHelloCommand);
        userInput.addCommand(sayNameCommand);
        userInput.removeCommand(sayHelloCommand);

        userInput.executeFromInput();
        assertTrue( outContent.toString().contains("Your name is: Alberto") );
        assertFalse( outContent.toString().contains("Hello") );
    }

    @Test
    @DisplayName("testShowOptions")
    public void testShowOptions()
    {
        UserInput userInput = new UserInput();
        userInput.addCommand(new Command("sayHello", (args) -> System.out.println("Hello!"), "desc1") );
        userInput.addCommand(new Command("sayName", (args) -> System.out.println("Your name is: " + args)));

        userInput.showOptions();

        assertTrue(  outContent.toString().contains(" -> sayHello: desc1") );
        assertTrue(  outContent.toString().contains(" -> sayName: No description.") );
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
