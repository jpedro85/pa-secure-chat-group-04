package Utils.Logger.Decorator;

import Utils.Logger.Logger;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.mockito.Mockito.*;

class TimeStampDecoratorTest {

    @Test
    void testAddsTimeStampToMessage() {
        Logger mockLogger = mock(Logger.class);
        TimeStampDecorator decorator = new TimeStampDecorator(mockLogger);
        String testMessage = "Test message";
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
        String expectedMessage = "\u001b[1;33m[" + timestamp + "]\u001b[0m " + testMessage;

        decorator.log(testMessage, Optional.empty());
        verify(mockLogger).log(expectedMessage, Optional.empty());
    }
}
