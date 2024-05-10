package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;

public class LogOutContentTest {

    @Test
    public void testGetByteMessage() {
        String username = "testUsername";
        LogOutContent logOutContent = new LogOutContent(username);
        assertEquals(username.getBytes(), logOutContent.getByteMessage());
    }

    @Test
    public void testGetStringMessage() {
        String username = "testUsername";
        LogOutContent logOutContent = new LogOutContent(username);
        assertEquals(username, logOutContent.getStringMessage());
    }

    @Test
    public void testGetType() {
        String username = "testUsername";
        LogOutContent logOutContent = new LogOutContent(username);
        assertEquals(ContentTypes.ACCOUNT, logOutContent.getType());
    }

    @Test
    public void testGetSubType() {
        String username = "testUsername";
        LogOutContent logOutContent = new LogOutContent(username);
        assertEquals(AccountMessageTypes.LOGOUT, logOutContent.getSubType());
    }

    @Test
    public void testGetDigest() {
        String username = "testUsername";
        LogOutContent logOutContent = new LogOutContent(username);
        assertNotNull(logOutContent.getDigest());
    }

    @Test
    public void testHasValidDigest() {
        String username = "testUsername";
        LogOutContent logOutContent = new LogOutContent(username);
        assertTrue(logOutContent.hasValidDigest());
    }
}
