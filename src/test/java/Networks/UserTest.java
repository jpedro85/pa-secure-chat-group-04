public class UserTest
{
    @Test
    public void testGetUsername() {
        User user = new User("JohnDoe");
        assertEquals("JohnDoe", user.getUsername());
    }

    @Test
    public void testGetCertificate() {
        User user = new User("JohnDoe");
        user.setCertificate("abc123");
        assertEquals("abc123", user.getCertificate());
    }

    @Test
    public void testSetCertificate() {
        User user = new User("JohnDoe");
        user.setCertificate("abc123");
        assertEquals("abc123", user.getCertificate());
    }

    @Test
    public void testToString() {
        User user = new User("JohnDoe");
        user.setCertificate("abc123");
        assertEquals("JohnDoeabc123", user.toString());
    }
}
