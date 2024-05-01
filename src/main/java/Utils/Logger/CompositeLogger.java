package Utils.Logger;

import Utils.Logger.Enums.LogTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompositeLogger implements Logger {

    private final List<Logger> loggers = new ArrayList<>();

    public List<Logger> getLoggers() {
        return loggers;
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    @Override
    public void log(String message, Optional<LogTypes> type) {
        for (Logger logger : loggers) {
            logger.log(message, type);
        }
    }
}
