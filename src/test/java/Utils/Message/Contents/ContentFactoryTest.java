package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Networks.User;
import Utils.Message.EnumTypes.CommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;

import java.util.ArrayList;

public class ContentFactoryTest {

    @Test
    public void testCreateTypeContent() {
        ContentSubtype type = CommunicationTypes.INVALID_CERTIFICATE;
        assertNotNull(ContentFactory.createTypeContent(type));
    }

    @Test
    public void testCreateTypeContentNullType() {
        ContentFactory.createTypeContent(null);
    }

    @Test
    public void testCreateIntegrityContent() {
        String content = "test";
        BigInteger secret = BigInteger.ONE;
        ContentSubtype type = CommunicationTypes.INVALID_CERTIFICATE;
        assertNotNull(ContentFactory.createIntegrityContent(content, secret, type));
    }

   

    @Test
    public void testCreateIntegrityContentNullArguments() {
        ContentFactory.createIntegrityContent(null, null, null);
    }

    
    @Test
    public void testCreateAllLoggedInContent() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        assertNotNull(ContentFactory.createAllLoggedInContent(users));
    }

    @Test
    public void testCreateAllLoggedInContentNullUsers() {
        ContentFactory.createAllLoggedInContent(null);
    }

    @Test
    public void testCreateMSGCommunicationContent() {
        byte[] message = "test".getBytes();
        assertNotNull(ContentFactory.createMSGCommunicationContent(message));
    }

    @Test
    public void testCreateMSGCommunicationContentNullMessage() {
        ContentFactory.createMSGCommunicationContent(null);
    }

   
}