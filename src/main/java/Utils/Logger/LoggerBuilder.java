package Utils.Logger;

import Utils.Logger.Decorator.*;
import Utils.Logger.Strategies.*;

/**
 * A builder class for creating and configuring a {@link Logger} instance.
 * This class supports the fluent interface style of programming, allowing for
 * method chaining to configure a {@link CompositeLogger} with various logging
 * capabilities, including console logging, file logging, and optional decorators
 * such as timestamp, upper case formatting, log type prefix, and plain text conversion.
 *
 * Each method in this builder returns the builder instance itself, enabling a
 * chain of method calls to configure the logger.
 */
public class LoggerBuilder {
    private final CompositeLogger compositeLogger = new CompositeLogger();
    private Logger currentLogger = null;

    /**
     * Configures the builder to add console logging to the {@link CompositeLogger}.
     *
     * @return This {@link LoggerBuilder} instance to allow for method chaining.
     */
    public LoggerBuilder useConsoleLogging() {
        currentLogger = new ConsoleLogger();
        compositeLogger.addLogger(currentLogger);
        return this;
    }

    /**
     * Configures the builder to add file logging to the {@link CompositeLogger}.
     * The log messages will be written to the specified file path.
     *
     * @param filePath The path to the file where log messages should be written.
     * @return This {@link LoggerBuilder} instance to allow for method chaining.
     */
    public LoggerBuilder useFileLogging(String filePath) {
        currentLogger = new FileLogger(filePath);
        compositeLogger.addLogger(currentLogger);
        return this;
    }

    /**
     * Adds a timestamp decorator to the current logger. Each log entry will be prefixed
     * with the current date and time.
     *
     * @return This {@link LoggerBuilder} instance to allow for method chaining.
     */
    public LoggerBuilder addTimeStamp() {
        if (currentLogger != null) {
            currentLogger = new TimeStampDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    /**
     * Adds an upper case decorator to the current logger. Each log message will be
     * converted to upper case.
     *
     * @return This {@link LoggerBuilder} instance to allow for method chaining.
     */
    public LoggerBuilder addUpperCase() {
        if (currentLogger != null) {
            currentLogger = new UpperCaseDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    /**
     * Adds a log type decorator to the current logger. Each log entry will include
     * the log type as a prefix.
     *
     * @return This {@link LoggerBuilder} instance to allow for method chaining.
     */
    public LoggerBuilder addType() {
        if (currentLogger != null) {
            currentLogger = new LogTypeDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    /**
     * Adds a plain text decorator to the current logger, ensuring log messages are
     * formatted without any special encoding or formatting.
     *
     * @return This {@link LoggerBuilder} instance to allow for method chaining.
     */
    public LoggerBuilder asPlainText() {
        if (currentLogger != null) {
            currentLogger = new PlainTextDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    /**
     * Replaces the last logger in the composite logger with a new logger. This is used
     * internally to update the decorator chain.
     *
     * @param newLogger The new {@link Logger} to replace the old one.
     */
    void replaceLastLogger(Logger newLogger) {
        int lastIndex = compositeLogger.getLoggers().size() - 1;
        compositeLogger.getLoggers().set(lastIndex, newLogger);
    }

    /**
     * Finalizes the configuration and returns the configured {@link CompositeLogger}.
     *
     * @return The configured {@link CompositeLogger} instance.
     */
    public Logger build() {
        return compositeLogger;
    }
}
