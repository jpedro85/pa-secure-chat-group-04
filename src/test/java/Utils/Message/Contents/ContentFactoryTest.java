package Utils.Message.Contents;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Security.Encryption.RSA;
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
    public void testCreateTypeContentNullType()
    {
        assertEquals( ContentFactory.createTypeContent(AccountMessageTypes.LOGIN).getSubType(), AccountMessageTypes.LOGIN );
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
        assertThrows( IllegalArgumentException.class, () -> {ContentFactory.createIntegrityContent(null, null, null);} );
    }


    @Test
    public void testCreateAllLoggedInContent() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1"));
        assertNotNull(ContentFactory.createAllLoggedInContent(users));
    }

    @Test
    public void testCreateAllLoggedInContentNullUsers() {
        assertThrows( IllegalArgumentException.class, () -> {ContentFactory.createAllLoggedInContent(null);} );
    }

    @Test
    public void testCreateMSGCommunicationContent() {
        byte[] message = "test".getBytes();
        assertNotNull(ContentFactory.createMSGCommunicationContent(message));
    }

    @Test
    public void testCreateMSGCommunicationContentNullMessage() {
        assertThrows( IllegalArgumentException.class, () -> {ContentFactory.createMSGCommunicationContent(null);} );
    }

    @Test
    public void testCreateSigneContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createSigneContent(null, null));
    }

    @Test
    public void testCreatePublicKeyContent() {
        assertNotNull(ContentFactory.createPublicKeyContent(RSA.generateKeyPair().getPublic(), BigInteger.ONE));
    }

    @Test
    public void testCreatePublicKeyContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createPublicKeyContent(null, null));
    }

    @Test
    public void testCreateErrorContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createErrorContent(null, null));
    }

    @Test
    public void testCreateRegisterContent() {
        assertNotNull(ContentFactory.createRegisterContent("username"));
    }

    @Test
    public void testCreateRegisterContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createRegisterContent(null));
    }

    @Test
    public void testCreateLoginContent() {
        assertNotNull(ContentFactory.createLoginContent("certificate", "username"));
    }

    @Test
    public void testCreateLoginContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createLoginContent(null, null));
    }

    @Test
    public void testCreateLoginRenovateContent() {
        assertNotNull(ContentFactory.createLoginRenovateContent("certificate", "username"));
    }

    @Test
    public void testCreateLoginRenovateContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createLoginRenovateContent(null, null));
    }

    @Test
    public void testCreateCertificateStateContent() {
        assertNotNull(ContentFactory.createCertificateStateContent(1, true));
    }

    @Test
    public void testCreateCertificateStateContentNullArguments() {
        assertNotNull( ContentFactory.createCertificateStateContent(1));
    }

    @Test
    public void testCreateCertificateStateInvalidContent() {
        assertNotNull(ContentFactory.createCertificateStateInvalidContent(1));
    }

    @Test
    public void testCreateLogoutContent() {
        assertNotNull(ContentFactory.createLogoutContent("username"));
    }

    @Test
    public void testCreateLogoutContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createLogoutContent(null));
    }

    @Test
    public void testCreateDiffieHellmanContent() {
        assertNotNull(ContentFactory.createDiffieHellmanContent(BigInteger.ONE));
    }

    @Test
    public void testCreateDiffieHellmanContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createDiffieHellmanContent(null));
    }

    @Test
    public void testCreateDiffieHellmanRSAContent() {
        assertNotNull(ContentFactory.createDiffieHellmanRSAContent(new byte[] {}));
    }

    @Test
    public void testCreateDiffieHellmanRSAContentNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> ContentFactory.createDiffieHellmanRSAContent(null));
    }

}