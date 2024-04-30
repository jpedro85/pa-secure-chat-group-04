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
        ColorSchemes color = ColorSchemes.GRAY;
        if (type.isPresent()) {
            switch (type.get()) {
                case WARN:
                    color = ColorSchemes.ORANGE;
                    break;
                case ERROR:
                    color = ColorSchemes.RED;
                    break;
                case DEBUG:
                    color = ColorSchemes.BLUE;
                    break;
                case INFO:
                default:
                    color = ColorSchemes.GRAY;
                    break;
            }
            message = color.toString() + "[" + type.get() + "] " + ColorSchemes.RESET + message;
        }
        super.log(message, type);
    }
}
