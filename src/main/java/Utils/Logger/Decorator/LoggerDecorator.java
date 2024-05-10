package Utils.Logger.Decorator;

import java.util.Optional;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;

/**
 * An abstract decorator class for enhancing the functionality of a {@link Logger} instance.
 * This class implements the {@link Logger} interface and is intended to be extended by other
 * decorators that add specific functionalities to the basic logging mechanism (e.g., adding timestamps,
 * log levels, or formatting). It holds a reference to a {@link Logger} instance and delegates the logging
 * call to this wrapped logger object, allowing additional behaviors to be executed before or after
 * the delegation.
 *
 * The direct use of this class is not intended; instead, it should be subclassed to provide specific enhancements.
 */
public abstract class LoggerDecorator implements Logger {

    protected Logger wrappedLogger;

    /**
     * Constructs a LoggerDecorator wrapping the specified {@link Logger} instance.
     *
     * @param logger The {@link Logger} to be decorated, which should not be null. It is the base
     *               logger that will ultimately perform the logging as enhanced by decorators.
     */
    public LoggerDecorator(Logger logger) {
        this.wrappedLogger = logger;
    }

    /**
     * Delegates the logging of a message to the wrapped {@link Logger} instance. This method
     * can be overridden by subclasses to add additional behavior before or after logging the message.
     *
     * @param message The message to be logged, not null.
     * @param type The optional type of the log message, used to categorize the log. If the type is present,
     *             it may influence how the message is formatted or processed by the logger.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        wrappedLogger.log(message, type);
    }
}
