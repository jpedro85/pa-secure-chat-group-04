package Utils.Logger.Decorator;

import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;

import java.util.Optional;
import java.util.regex.Pattern;

public class PlainTextDecorator extends LoggerDecorator {

    private static final Pattern ANSI_PATTERN = Pattern.compile("\\x1B\\[\\d+;?\\d*;?\\d*;?\\d*;?\\d*m");

    public PlainTextDecorator(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    @Override
    public void log(String message, Optional<LogTypes> type) {
        String plainMessage = ANSI_PATTERN.matcher(message).replaceAll("");
        super.log(plainMessage, type);
    }
}
