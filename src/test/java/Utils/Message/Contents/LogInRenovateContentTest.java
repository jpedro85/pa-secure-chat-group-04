package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.AccountMessageTypes;

import static org.junit.jupiter.api.Assertions.*;

public class LogInRenovateContentTest {

    @Test
    public void testGetSubType() {
        String certificate = "certificate";
        String username = "username";
        LogInRenovateContent logInRenovateContent = new LogInRenovateContent(certificate, username);
        assertEquals(AccountMessageTypes.LOGIN_RENOVATE, logInRenovateContent.getSubType());
    }
}

