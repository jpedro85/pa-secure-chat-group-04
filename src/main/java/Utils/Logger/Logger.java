package Utils.Logger;

import Utils.Logger.Enums.LogTypes;
import java.util.Optional;

/**
 * Provides an abstraction for logging messages with optional log types.
 * This interface allows for implementation-specific logging details such as logging level and format.
 * Implementations can define how messages are recorded, such as to the console, files, or external systems.
 */
public interface Logger {

    /**
     * Logs a message with an optional type.
     * The type parameter allows categorizing the log message, see {@link LogTypes}
     * If the type is not provided, the implementation should handle it as a default or undefined category.
     *
     * @param message The message string to be logged. It should not be null.
     * @param type The optional type of the log. If empty, defaults to the implementation's basic logging type.
     */
    void log(String message, Optional<LogTypes> type);
}
