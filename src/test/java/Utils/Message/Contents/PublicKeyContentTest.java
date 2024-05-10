package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeyContentTest {

    @Test
    public void testGetPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        BigInteger secret = BigInteger.valueOf(123456);
        PublicKeyContent publicKeyContent = new PublicKeyContent(publicKey, secret);
        
        byte[] encodedPublicKey = publicKey.getEncoded();
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey decodedPublicKey = keyFactory.generatePublic(publicKeySpec);

        assertEquals(decodedPublicKey, publicKeyContent.getPublicKey());
    }

    @Test
    public void testGetByteMessage() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        BigInteger secret = BigInteger.valueOf(123456);
        PublicKeyContent publicKeyContent = new PublicKeyContent(publicKey, secret);

        byte[] encodedPublicKey = publicKey.getEncoded();
        assertArrayEquals(encodedPublicKey, publicKeyContent.getByteMessage());
    }

    @Test
    public void testGetStringMessage() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        BigInteger secret = BigInteger.valueOf(123456);
        PublicKeyContent publicKeyContent = new PublicKeyContent(publicKey, secret);

        String encodedPublicKey = publicKey.toString();
        assertEquals(encodedPublicKey, publicKeyContent.getStringMessage());
    }

    @Test
    public void testGetType() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        BigInteger secret = BigInteger.valueOf(123456);
        PublicKeyContent publicKeyContent = new PublicKeyContent(publicKey, secret);

        assertEquals(ContentTypes.CA_COMMUNICATION, publicKeyContent.getType());
    }

    @Test
    public void testGetSubType() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        BigInteger secret = BigInteger.valueOf(123456);
        PublicKeyContent publicKeyContent = new PublicKeyContent(publicKey, secret);

        assertEquals(CACommunicationTypes.PUBLIC_KEY, publicKeyContent.getSubType());
    }

    @Test
    public void testGetMAC() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        BigInteger secret = BigInteger.valueOf(123456);
        PublicKeyContent publicKeyContent = new PublicKeyContent(publicKey, secret);

        assertNotNull(publicKeyContent.getMAC());
    }

    @Test
    public void testHasValidMAC() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();

        BigInteger secret = BigInteger.valueOf(123456);
        PublicKeyContent publicKeyContent = new PublicKeyContent(publicKey, secret);

        assertTrue(publicKeyContent.hasValidMAC(secret.toByteArray()));
    }
}
