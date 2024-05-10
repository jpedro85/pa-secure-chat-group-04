package Utils.Logger.Decorator;

import Utils.Logger.Logger;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.mockito.Mockito.*;

class UpperCaseDecoratorTest {

    @Test
    void testUpperCaseDecorator() {
        Logger mockLogger = mock(Logger.class);
        Logger decorator = new UpperCaseDecorator(mockLogger);

        decorator.log("test", Optional.empty());
        verify(mockLogger).log("TEST", Optional.empty());
    }
}
