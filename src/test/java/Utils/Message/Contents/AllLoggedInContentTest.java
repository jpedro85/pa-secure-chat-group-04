package Utils.Message.Contents;
import Utils.Security.Integrity.HASH;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Networks.User;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class AllLoggedInContentTest {

    @Test
    @DisplayName("testConstructorNullUsers")
    public void testConstructorNullUsers() {

        assertThrows(IllegalArgumentException.class, () -> {
            new AllLoggedInContent(null);
        });
    }

    @Test
    @DisplayName("testDigest")
    public void testDigest()
    {
        ArrayList<User> arrayList = new ArrayList<>(1);
        arrayList.add(new User("A"));
        AllLoggedInContent content = new AllLoggedInContent( arrayList );
        assertArrayEquals( content.getDigest(), HASH.generateDigest( content.getByteMessage() ));
    }

    @Test
    @DisplayName("testGetUsers")
    public void testGetUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        users.add(new User("user2"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(users, allLoggedInContent.getUsers());
    }

    @Test
    @DisplayName("testGetStringMessage")
    public void testGetStringMessage() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        users.add(new User("user2"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(users.toString(), allLoggedInContent.getStringMessage());
    }

    @Test
    @DisplayName("testGetType")
    public void testGetType() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(ContentTypes.ACCOUNT, allLoggedInContent.getType());
    }

    @Test
    @DisplayName("testGetSubType")
    public void testGetSubType() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertEquals(AccountMessageTypes.LOGGED_USERS, allLoggedInContent.getSubType());
    }

    @Test
    @DisplayName("testHasValidDigest")
    public void testHasValidDigest() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        AllLoggedInContent allLoggedInContent = new AllLoggedInContent(users);
        assertTrue(allLoggedInContent.hasValidDigest());
    }
}