package Utils.Logger.Decorator;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Enums.ColorSchemes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TimeStampDecorator extends LoggerDecorator {

    public TimeStampDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    @Override
    public void log(String message, Optional<LogTypes> type) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
        String decoratedMessage = ColorSchemes.YELLOW + "[" + timestamp + "]" + ColorSchemes.RESET + " " + message;
        super.log(decoratedMessage, type);
    }
}
