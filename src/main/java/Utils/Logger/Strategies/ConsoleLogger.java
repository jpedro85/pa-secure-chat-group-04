package Utils.Logger.Strategies;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;

import java.util.Optional;

/**
 * A simple logger that outputs messages to the system console.
 * This class implements the {@link Logger} interface and provides basic logging functionality by
 * printing messages directly to the standard output (System.out). It is intended for quick debugging
 * or environments where simple logging to the console is sufficient.
 *
 * This logger does not use log types to modify the output format, but log types can be integrated
 * by extending this class or through decorators to add functionality like log level prefixes or color coding.
 *
 * Usage example:
 * Logger logger = new ConsoleLogger();
 * logger.log("An example info message", Optional.of(LogTypes.INFO));
 * // The output will be: "An example info message"
 */
public class ConsoleLogger implements Logger {

    /**
     * Logs a message to the console.
     * This implementation ignores the {@code type} parameter and directly prints the {@code message}
     * to the standard output. The simplicity of this method makes it suitable for scenarios where
     * the details of the log type are not required or are handled elsewhere.
     *
     * @param message The message string to be logged. This should not be null.
     * @param type The optional type of the log, used to categorize the log message.
     *             This parameter is not utilized in the current implementation but can be used
     *             in extended classes or with decorators.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        System.out.println(message);
    }
}
