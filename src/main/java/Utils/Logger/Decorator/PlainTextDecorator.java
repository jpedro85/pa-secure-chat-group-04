package Utils.Logger.Decorator;

import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;

import java.util.Optional;
import java.util.regex.Pattern;
/**
 * A decorator that removes ANSI color codes from log messages, ensuring that logs are
 * outputted in plain text. This class extends {@link LoggerDecorator} and overrides
 * the {@link #log(String, Optional)} method to strip any ANSI codes before passing
 * the message to the wrapped {@link Logger} instance.
 *
 * This decorator is particularly useful when logs are intended for environments that
 * do not support ANSI colors, such as plain text files or certain console windows.
 *
 * Usage:
 * Logger logger = new ConsoleLogger();
 * logger = new PlainTextDecorator(logger);
 * logger.log("\u001B[31mThis is a red message\u001B[0m", Optional.empty());
 * // The output will be: "This is a red message"
 */
public class PlainTextDecorator extends LoggerDecorator {

    private static final Pattern ANSI_PATTERN = Pattern.compile("\\x1B\\[\\d+;?\\d*;?\\d*;?\\d*;?\\d*m");

    /**
     * Constructs a PlainTextDecorator wrapping the specified {@link Logger} instance.
     *
     * @param wrappedLogger The {@link Logger} to be decorated, which should not be null.
     */
    public PlainTextDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    /**
     * Logs a message after removing all ANSI color codes, making it plain text. This method
     * matches and replaces ANSI sequences in the message string using a regular expression
     * before passing the clean, plain text message to the decorated logger.
     *
     * @param message The message string to be logged, potentially containing ANSI color codes.
     * @param type The optional type of the log, used to categorize the log message.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        String plainMessage = ANSI_PATTERN.matcher(message).replaceAll("");
        super.log(plainMessage, type);
    }
}
