package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Networks.User;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class AllLoggedInContentTest {

    @Test
    public void testConstructorNullUsers() {
        new AllLoggedInContent(null);
    }

    @Test
    public void testGetUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        users.add(new User("user2"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(users, allLoggedInContent.getUsers());
    }

    @Test
    public void testGetStringMessage() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        users.add(new User("user2"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(users.toString(), allLoggedInContent.getStringMessage());
    }

    @Test
    public void testGetType() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(ContentTypes.ACCOUNT, allLoggedInContent.getType());
    }

    @Test
    public void testGetSubType() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(AccountMessageTypes.LOGGED_USERS, allLoggedInContent.getSubType());
    }

    @Test
    public void testHasValidDigest() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertTrue(allLoggedInContent.hasValidDigest());
    }
}