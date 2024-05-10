package Utils.Logger;

import Utils.Logger.Enums.LogTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CompositeLoggerTest {

    private CompositeLogger compositeLogger;
    private Logger mockLogger1;
    private Logger mockLogger2;

    @BeforeEach
    void setUp() {
        // Instantiate the CompositeLogger and mock loggers
        compositeLogger = new CompositeLogger();
        mockLogger1 = mock(Logger.class);
        mockLogger2 = mock(Logger.class);

        // Add mock loggers to the CompositeLogger
        compositeLogger.addLogger(mockLogger1);
        compositeLogger.addLogger(mockLogger2);
    }
    @Test
    void shouldDelegateLogMessagesToAllContainedLoggers() {
        String testMessage = "Test message";
        Optional<LogTypes> logType = Optional.empty();

        compositeLogger.log(testMessage, logType);

        verify(mockLogger1).log(testMessage, logType);
        verify(mockLogger2).log(testMessage, logType);
    }

    @Test
    void shouldCorrectlyLogMultipleDifferentMessages() {
        Logger mockLogger = mock(Logger.class);
        compositeLogger.addLogger(mockLogger);

        String[] messages = {"First message", "Second message", "Third message"};
        for (String message : messages) {
            compositeLogger.log(message, Optional.empty());
        }

        // Verify that the mock logger received all messages
        for (String message : messages) {
            verify(mockLogger).log(message, Optional.empty());
        }
    }

    @Test
    void shouldHandleLoggingWhenNoLoggersAreContained() {
        String testMessage = "Test message";
        Optional<LogTypes> logType = Optional.empty();

        // Expect no exceptions or errors
        assertDoesNotThrow(() -> compositeLogger.log(testMessage, logType),
                "Logging with no contained loggers should not throw an exception.");
    }

}
