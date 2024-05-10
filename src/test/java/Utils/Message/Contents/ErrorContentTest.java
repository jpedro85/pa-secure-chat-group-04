package Utils.Message.Contents;
import org.junit.jupiter.api.Test;
import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.EnumTypes.ContentTypes;

import static Utils.Message.EnumTypes.AccountMessageTypes.LOGGED_USERS;
import static Utils.Message.EnumTypes.AccountMessageTypes.LOGIN;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorContentTest {

    @Test
    public void testGetByteMessage() {
        String error = "Sample error message";
        MessageContent content = new TypeContent(LOGIN);
        ErrorContent errorContent = new ErrorContent(content, error);
        assertArrayEquals(error.getBytes(), errorContent.getByteMessage());
    }

    @Test
    public void testGetStringMessage() {
        String error = "Sample error message";
        MessageContent content = new TypeContent(LOGIN);
        ErrorContent errorContent = new ErrorContent(content, error);
        assertEquals(error, errorContent.getStringMessage());
    }

    @Test
    public void testGetType() {
        String error = "Sample error message";
        MessageContent content = new TypeContent(LOGIN);
        ErrorContent errorContent = new ErrorContent(content, error);
        assertEquals(ContentTypes.ERROR, errorContent.getType());
    }

    @Test
    public void testGetSubType() {
        String error = "Sample error message";
        MessageContent content = new TypeContent(LOGGED_USERS);
        ErrorContent errorContent = new ErrorContent(content, error);
        assertEquals(LOGGED_USERS, errorContent.getSubType());
    }
}
