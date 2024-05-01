package Utils.Logger.Strategies;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;

import java.util.Optional;

public class ConsoleLogger implements Logger {

    @Override
    public void log(String message, Optional<LogTypes> type) {
        System.out.println(message);
    }
}
