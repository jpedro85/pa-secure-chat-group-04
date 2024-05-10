package Utils.Logger.Enums;

/**
 * Represents the various types of log messages that can be generated.
 * Each log type indicates the severity or importance of the message being logged,
 * helping in categorization and filtering of log output based on the log level.
 *
 * Log types include:
 * - {@code INFO}: Used for informational messages that highlight the progress of the application
 *   at a coarse-grained level. Ideal for understanding the flow through the system and for
 *   state changes that are not necessarily an issue.
 * - {@code DEBUG}: Used for detailed informational events that are most useful to debug an
 *   application. These events are usually only captured when the system is undergoing diagnostics
 *   or detailed logging is required for troubleshooting.
 * - {@code WARN}: Indicates a potential issue or important, but not necessarily error-level, situation.
 *   This log type is used to draw attention to something that could potentially go wrong, allowing
 *   for proactive intervention.
 * - {@code ERROR}: Used to log error events that might still allow the application to continue running,
 *   but which indicate problems that should definitely be investigated and might require corrective measures.
 *
 * Utilizing these log types allows developers and system administrators to quickly determine the
 * significance of log messages and manage them accordingly.
 */
public enum LogTypes {
    INFO,   // Informational messages about application progress or state changes.
    DEBUG,  // Detailed info useful for debugging applications.
    WARN,   // Warnings about potential issues.
    ERROR   // Errors affecting operation, but not necessarily stopping the application.
}
