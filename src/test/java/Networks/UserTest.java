package Networks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest
{
    @Test
    @DisplayName("Testing getUsername")
    public void testGetUsername() {
        User user = new User("JohnDoe");
        assertEquals("JohnDoe", user.getUsername());
    }

    @Test
    @DisplayName("Testing getCertificate")
    public void testGetCertificate() {
        User user = new User("JohnDoe");
        user.setCertificate("abc123");
        assertEquals("abc123", user.getCertificate());
    }

    @Test
    @DisplayName("Testing testSetCertificate")
    public void testSetCertificate() {
        User user = new User("JohnDoe");
        user.setCertificate("abc123");
        assertEquals("abc123", user.getCertificate());
    }

    @Test
    @DisplayName("Testing testToString")
    public void testToString() {
        User user = new User("JohnDoe");
        user.setCertificate("Abc123");
        assertEquals("JohnDoeAbc123", user.toString());
    }

}
