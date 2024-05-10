package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;

public class LogInContentTest {

    @Test
    public void testGetUSERNAME() {
        String username = "testUser";
        LogInContent loginContent = new LogInContent("certificate", username);
        assertEquals(username, loginContent.getUSERNAME());
    }

    @Test
    public void testGetCertificate() {
        String certificate = "testCertificate";
        LogInContent loginContent = new LogInContent(certificate, "username");
        assertEquals(certificate, loginContent.getCertificate());
    }

    @Test
    public void testGetStringMessage() {
        String username = "testUser";
        String certificate = "testCertificate";
        LogInContent loginContent = new LogInContent(certificate, username);
        assertEquals(username + certificate, loginContent.getStringMessage());
    }

    @Test
    public void testGetType() {
        LogInContent loginContent = new LogInContent("certificate", "username");
        assertEquals(ContentTypes.ACCOUNT, loginContent.getType());
    }

    @Test
    public void testGetSubType() {
        LogInContent loginContent = new LogInContent("certificate", "username");
        assertEquals(AccountMessageTypes.LOGIN, loginContent.getSubType());
    }

    @Test
    public void testHasValidDigest() {
        String username = "testUser";
        String certificate = "testCertificate";
        LogInContent loginContent = new LogInContent(certificate, username);
        assertTrue(loginContent.hasValidDigest());
    }
}