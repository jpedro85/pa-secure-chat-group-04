import Utils.Logger.Decorator.UpperCaseDecorator;
import Utils.Logger.Enums.LogTypes;

import Utils.Logger.Logger;
import Utils.Logger.LoggerBuilder;
import Utils.Logger.Strategies.ConsoleLogger;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        LoggerBuilder builder = new LoggerBuilder();
        Logger logger = builder
                .useConsoleLogging()
                .addTimeStamp()
                .addType()
                .build();
        Logger loggers = new ConsoleLogger();
        Logger decorator = new UpperCaseDecorator(loggers);

        logger.log("An informational message", Optional.of(LogTypes.INFO));
        logger.log("A warning message", Optional.of(LogTypes.WARN));
        logger.log("A debug message", Optional.of(LogTypes.DEBUG));
        logger.log("A Error message", Optional.of(LogTypes.ERROR));
    }
}
