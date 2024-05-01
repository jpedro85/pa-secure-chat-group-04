package Utils.Logger.Strategies;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.util.Optional;

import static org.mockito.Mockito.verify;

class ConsoleLoggerTest {
    @Test
    void testLogPrintsToConsole() {
        PrintStream originalOut = System.out;
        PrintStream mockedOut = Mockito.mock(PrintStream.class);
        System.setOut(mockedOut);

        ConsoleLogger logger = new ConsoleLogger();
        String testMessage = "Test message";
        logger.log(testMessage, Optional.empty());

        verify(mockedOut).println(testMessage);
        System.setOut(originalOut);
    }
}