package Utils.Logger.Decorator;

import java.util.Optional;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;

public abstract class LoggerDecorator implements Logger {

    protected Logger wrappedLogger;

    public LoggerDecorator(Logger logger) {
        this.wrappedLogger = logger;
    }

    @Override
    public void log(String message, Optional<LogTypes> type) {
        wrappedLogger.log(message, type);
    }
}
