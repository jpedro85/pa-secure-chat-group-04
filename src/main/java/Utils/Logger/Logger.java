package Utils.Logger;

import Utils.Logger.Enums.LogTypes;
import java.util.Optional;

public interface Logger {
    void log(String message, Optional<LogTypes> type);
}
