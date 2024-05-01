package Utils.Logger.Decorator;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Enums.ColorSchemes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * A decorator that adds a timestamp prefix to each log message. This class extends {@link LoggerDecorator}
 * and provides functionality to prepend the log messages with the current date and time formatted as
 * "yyyy-MM-dd_HH:mm:ss". The timestamp is highlighted in yellow using ANSI color codes, which can be
 * especially helpful for tracking the timing of log entries in a visually distinct manner.
 *
 * This decorator is useful in scenarios where log entry timing is crucial, such as in debugging sessions
 * or when monitoring application behavior over time.
 *
 * Usage:
 * Logger logger = new ConsoleLogger();
 * logger = new TimeStampDecorator(logger);
 * logger.log("An example log message", Optional.empty());
 * // The output will be: "[2023-04-25_14:20:30] An example log message" (with the timestamp in yellow)
 */
public class TimeStampDecorator extends LoggerDecorator {

    /**
     * Constructs a TimeStampDecorator wrapping the specified {@link Logger} instance.
     *
     * @param wrappedLogger The {@link Logger} to be decorated, which should not be null.
     */
    public TimeStampDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    /**
     * Logs a message with a timestamp prefix. The timestamp is formatted as "yyyy-MM-dd_HH:mm:ss"
     * and colored in yellow to enhance visibility. The decorated message is then passed to the
     * wrapped logger for actual logging.
     *
     * @param message The message string to be logged, which should not be null.
     * @param type The optional type of the log, used to categorize the log message. This type
     *             does not directly affect the timestamp but may influence how the subsequent
     *             logger handles the message.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
        String decoratedMessage = ColorSchemes.YELLOW + "[" + timestamp + "]" + ColorSchemes.RESET + " " + message;
        super.log(decoratedMessage, type);
    }
}
