package Utils.Logger.Decorator;

import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.mockito.Mockito.*;

class LogTypeDecoratorTest {

    @Test
    void testAddsLogTypeInformation() {
        Logger mockLogger = mock(Logger.class);
        LogTypeDecorator decorator = new LogTypeDecorator(mockLogger);
        String testMessage = "Test message";

        decorator.log(testMessage, Optional.of(LogTypes.ERROR));
        String expectedMessage = "\u001b[1;31m[ERROR] \u001b[0mTest message";

        verify(mockLogger).log(expectedMessage, Optional.of(LogTypes.ERROR));
    }
}
