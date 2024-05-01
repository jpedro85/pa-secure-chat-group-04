package Utils.Logger.Decorator;

import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Enums.ColorSchemes;
import Utils.Logger.Logger;

import java.util.Optional;

/**
 * A decorator for {@link Logger} that prefixes log messages with a log type indicator and color coding.
 * This class extends {@link LoggerDecorator} and overrides the {@link #log(String, Optional)} method to
 * include the log type in a specified color before the log message. The colors are specified by the {@link ColorSchemes} enum.
 *
 * The log type prefix helps in easily identifying the severity or category of log messages,
 * which is particularly useful in a complex logging environment where messages are generated from various sources
 * or components.
 *
 * Usage:
 * Logger logger = new ConsoleLogger();
 * logger = new LogTypeDecorator(logger);
 * logger.log("An error occurred", Optional.of(LogTypes.ERROR));
 */
public class LogTypeDecorator extends LoggerDecorator {

    /**
     * Constructs a new {@code LogTypeDecorator} wrapping the specified {@link Logger}.
     *
     * @param wrappedLogger the {@link Logger} to be decorated, which cannot be null.
     */
    public LogTypeDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    /**
     * Logs a message with a type prefix and color coding. If the {@code type} is present,
     * it adds a color-coded log type prefix to the message. Colors are defined in {@link ColorSchemes}.
     *
     * @param message The message string to be logged, which should not be null.
     * @param type The optional log type to categorize the log message.
     *             If the type is not present, the message is logged without type prefix.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        ColorSchemes color;
        if (type.isPresent()) {
            color = switch (type.get()) {
                case WARN -> ColorSchemes.ORANGE;
                case ERROR -> ColorSchemes.RED;
                case DEBUG -> ColorSchemes.BLUE;
                default -> ColorSchemes.GRAY;
            };
            message = color + "[" + type.get() + "] " + ColorSchemes.RESET + message;
        }
        super.log(message, type);
    }
}
