package Utils.Logger.Decorator;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;

import java.util.Optional;

public class UpperCaseDecorator extends LoggerDecorator {

    public UpperCaseDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    @Override
    public void log(String message, Optional<LogTypes> type) {
        super.log(message.toUpperCase(), type);
    }
}
