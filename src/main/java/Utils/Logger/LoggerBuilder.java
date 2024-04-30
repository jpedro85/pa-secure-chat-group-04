package Utils.Logger;

import Utils.Logger.Decorator.*;
import Utils.Logger.Strategies.*;

public class LoggerBuilder {
    private CompositeLogger compositeLogger = new CompositeLogger();
    private Logger currentLogger = null;

    public LoggerBuilder useConsoleLogging() {
        currentLogger = new ConsoleLogger();
        compositeLogger.addLogger(currentLogger);
        return this;
    }

    public LoggerBuilder useFileLogging(String filePath) {
        currentLogger = new FileLogger(filePath);
        compositeLogger.addLogger(currentLogger);
        return this;
    }

    public LoggerBuilder addTimeStamp() {
        if (currentLogger != null) {
            currentLogger = new TimeStampDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    public LoggerBuilder addUpperCase() {
        if (currentLogger != null) {
            currentLogger = new UpperCaseDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    public LoggerBuilder addType() {
        if (currentLogger != null) {
            currentLogger = new LogTypeDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    public LoggerBuilder asPlainText() {
        if (currentLogger != null) {
            currentLogger = new PlainTextDecorator(currentLogger);
            replaceLastLogger(currentLogger);
        }
        return this;
    }

    private void replaceLastLogger(Logger newLogger) {
        int lastIndex = compositeLogger.getLoggers().size() - 1;
        compositeLogger.getLoggers().set(lastIndex, newLogger);
    }

    public Logger build() {
        return compositeLogger;
    }
}
