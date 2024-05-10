package Utils.Logger.Decorator;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;

import java.util.Optional;

/**
 * A decorator that converts all logged messages to uppercase before passing them to the decorated logger.
 * This class extends {@link LoggerDecorator} and modifies the log messages by transforming them into uppercase,
 * which can help in making log messages stand out or conform to certain logging formats that require uppercase.
 *
 * This decorator is useful in environments where consistency in log message casing is required, or when
 * uppercasing could help in distinguishing log messages in visual inspections.
 *
 * Usage:
 * Logger logger = new ConsoleLogger();
 * logger = new UpperCaseDecorator(logger);
 * logger.log("error occurred", Optional.of(LogTypes.ERROR));
 * // The output will be: "ERROR OCCURRED"
 */
public class UpperCaseDecorator extends LoggerDecorator {

    /**
     * Constructs an UpperCaseDecorator wrapping the specified {@link Logger} instance.
     *
     * @param wrappedLogger The {@link Logger} to be decorated, which should not be null.
     */
    public UpperCaseDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    /**
     * Logs a message after converting it to uppercase. This method modifies the message string to
     * uppercase before delegating the logging to the wrapped logger. This transformation is applied
     * to every log message passed through this decorator.
     *
     * @param message The message string to be logged, which should not be null.
     * @param type The optional type of the log, used to categorize the log message. The type does
     *             not affect the uppercase transformation.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        super.log(message.toUpperCase(), type);
    }
}
