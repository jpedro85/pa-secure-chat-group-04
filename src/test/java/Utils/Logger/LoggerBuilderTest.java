package Utils.Logger;

import Utils.Logger.Decorator.*;
import Utils.Logger.Strategies.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class LoggerBuilderTest {

    private LoggerBuilder loggerBuilder;
    private CompositeLogger compositeLogger;
    private List<Logger> loggers;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        compositeLogger = mock(CompositeLogger.class);
        loggerBuilder = new LoggerBuilder();
        loggers = new ArrayList<>();

        Field compositeLoggerField = LoggerBuilder.class.getDeclaredField("compositeLogger");
        compositeLoggerField.setAccessible(true);
        compositeLoggerField.set(loggerBuilder, compositeLogger);

        when(compositeLogger.getLoggers()).thenReturn(loggers);

        doAnswer(invocation -> {
            Logger logger = invocation.getArgument(0);
            loggers.add(logger);
            return null;
        }).when(compositeLogger).addLogger(any(Logger.class));
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(compositeLogger);
    }

    @Test
    void useConsoleLoggingShouldAddConsoleLoggerToComposite() {
        loggerBuilder.useConsoleLogging();
        verify(compositeLogger).addLogger(any(ConsoleLogger.class));
    }

    @Test
    void useFileLoggingShouldAddFileLoggerToComposite() throws IOException {
        String filePath = "log.txt";
        loggerBuilder.useFileLogging(filePath);
        verify(compositeLogger).addLogger(any(FileLogger.class));

        Path logFilePath = Paths.get(filePath);
        try {
            Files.deleteIfExists(logFilePath);
        } catch (IOException e) {
            System.err.println("Failed to delete the log file: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void addTimeStampShouldWrapCurrentLoggerWithTimeStampDecorator()
            throws IllegalAccessException, NoSuchFieldException {
        loggerBuilder.useConsoleLogging();
        loggerBuilder.addTimeStamp();

        Logger lastLogger = compositeLogger.getLoggers().get(compositeLogger.getLoggers().size() - 1);
        assertInstanceOf(TimeStampDecorator.class, lastLogger, "Logger should be wrapped by TimeStampDecorator");
    }

    @Test
    void addUpperCaseShouldWrapCurrentLoggerWithUpperCaseDecorator()
            throws IllegalAccessException, NoSuchFieldException {
        loggerBuilder
                .useConsoleLogging()
                .addUpperCase();

        Logger lastLogger = compositeLogger.getLoggers().get(compositeLogger.getLoggers().size() - 1);
        assertInstanceOf(UpperCaseDecorator.class, lastLogger, "Logger should be wrapped by UpperCaseDecorator");
    }

    @Test
    void addLogTypeShouldWrapCurrentLoggerWithLogTypeDecorator() throws IllegalAccessException, NoSuchFieldException {
        loggerBuilder
                .useConsoleLogging()
                .addType();

        Logger lastLogger = compositeLogger.getLoggers().get(compositeLogger.getLoggers().size() - 1);
        assertInstanceOf(LogTypeDecorator.class, lastLogger, "Logger should be wrapped by LogTypeDecorator");
    }

    @Test
    void asPlainTextdWrapCurrentLoggerWithPlainTextDecorator() throws IllegalAccessException, NoSuchFieldException {
        loggerBuilder
                .useConsoleLogging()
                .asPlainText();

        Logger lastLogger = compositeLogger.getLoggers().get(compositeLogger.getLoggers().size() - 1);
        assertInstanceOf(PlainTextDecorator.class, lastLogger, "Logger should be wrapped by PlainTextDecorator");
    }

    @Test
    void buildShouldReturnCompositeLogger() {
        Logger result = loggerBuilder.build();
        assertNotNull(result);
        assertInstanceOf(CompositeLogger.class, result);
    }
}
