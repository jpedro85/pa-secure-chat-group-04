package Utils.Logger.Decorator;

import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Enums.ColorSchemes;
import Utils.Logger.Logger;

import java.util.Optional;

public class LogTypeDecorator extends LoggerDecorator {

    public LogTypeDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    @Override
    public void log(String message, Optional<LogTypes> type) {
        ColorSchemes color;
        if (type.isPresent()) {
            color = switch (type.get()) {
                case WARN -> ColorSchemes.ORANGE;
                case ERROR -> ColorSchemes.RED;
                case DEBUG -> ColorSchemes.BLUE;
                default -> ColorSchemes.GRAY;
            };
            message = color + "[" + type.get() + "] " + ColorSchemes.RESET + message;
        }
        super.log(message, type);
    }
}
