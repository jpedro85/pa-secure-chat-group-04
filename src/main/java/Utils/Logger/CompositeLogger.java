package Utils.Logger;

import Utils.Logger.Enums.LogTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A composite logger that manages a collection of logger instances.
 * This class implements the {@link Logger} interface and provides a means to log messages
 * through multiple loggers simultaneously. It is useful for scenarios where log output
 * needs to be sent to multiple destinations (e.g., console, files, external systems).
 *
 * Usage involves adding one or more logger instances via {@link #addLogger(Logger)} and
 * then using {@link #log(String, Optional)} to dispatch log messages to all registered loggers.
 */
public class CompositeLogger implements Logger {

    private final List<Logger> loggers = new ArrayList<>();

    /**
     * Returns the list of all loggers currently registered in this composite.
     * This list is not modifiable directly. Use {@link #addLogger(Logger)} to add loggers.
     *
     * @return An unmodifiable list of registered {@link Logger} instances.
     */
    public List<Logger> getLoggers() {
        return loggers;
    }

    /**
     * Adds a logger to the composite collection. Each added logger will receive the log messages
     * sent to this composite logger.
     *
     * @param logger The {@link Logger} instance to add to the composite. It should not be null.
     */
    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

   /**
     * Logs a message to all registered loggers. Each logger in the composite will log the message
     * according to its own log type handling and format.
     *
     * @param message The message string to be logged. It should not be null.
     * @param type The optional type of the log, used to categorize the log message.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        for (Logger logger : loggers) {
            logger.log(message, type);
        }
    }
}
