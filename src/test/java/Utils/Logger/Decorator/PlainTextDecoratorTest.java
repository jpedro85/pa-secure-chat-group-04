package Utils.Logger.Decorator;

import Utils.Logger.Logger;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.mockito.Mockito.*;

class PlainTextDecoratorTest {

    @Test
    void testStripsAnsiEscapeCodes() {
        Logger mockLogger = mock(Logger.class);
        PlainTextDecorator decorator = new PlainTextDecorator(mockLogger);
        String messageWithAnsi = "\u001B[31mRed Message\u001B[0m";

        decorator.log(messageWithAnsi, Optional.empty());
        verify(mockLogger).log("Red Message", Optional.empty());
    }
}
