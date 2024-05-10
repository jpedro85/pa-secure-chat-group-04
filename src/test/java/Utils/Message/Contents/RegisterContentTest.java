package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;
public class RegisterContentTest {

    @Test
    public void testGetByteMessage() {
        String username = "testUsername";
        RegisterContent registerContent = new RegisterContent(username);
        assertArrayEquals(username.getBytes(), registerContent.getByteMessage());
    }

    @Test
    public void testGetStringMessage() {
        String username = "testUsername";
        RegisterContent registerContent = new RegisterContent(username);
        assertEquals(username, registerContent.getStringMessage());
    }

    @Test
    public void testGetType() {
        String username = "testUsername";
        RegisterContent registerContent = new RegisterContent(username);
        assertEquals(ContentTypes.ACCOUNT, registerContent.getType());
    }

    @Test
    public void testGetSubType() {
        String username = "testUsername";
        RegisterContent registerContent = new RegisterContent(username);
        assertEquals(AccountMessageTypes.REGISTER, registerContent.getSubType());
    }

    @Test
    public void testGetDigest() {
        String username = "testUsername";
        RegisterContent registerContent = new RegisterContent(username);
        assertNotNull(registerContent.getDigest());
    }

    @Test
    public void testHasValidDigest() {
        String username = "testUsername";
        RegisterContent registerContent = new RegisterContent(username);
        assertTrue(registerContent.hasValidDigest());
    }
}

