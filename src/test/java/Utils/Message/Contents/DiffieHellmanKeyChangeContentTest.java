package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.DiffieHellmanTypes;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;

public class DiffieHellmanKeyChangeContentTest {

    @Test
    public void testGetPublic_key() {
        byte[] publicKeyBytes = BigInteger.valueOf(123456).toByteArray();
        DiffieHellmanKeyChangeContent content = new DiffieHellmanKeyChangeContent(publicKeyBytes);
        BigInteger publicKey = new BigInteger(publicKeyBytes);
        assertEquals(publicKey, content.getPublic_key());
    }

    @Test
    public void testGetByteMessage() {
        byte[] publicKeyBytes = BigInteger.valueOf(123456).toByteArray();
        DiffieHellmanKeyChangeContent content = new DiffieHellmanKeyChangeContent(publicKeyBytes);
        assertArrayEquals(publicKeyBytes, content.getByteMessage());
    }

    @Test
    public void testGetStringMessage() {
        byte[] publicKeyBytes = BigInteger.valueOf(123456).toByteArray();
        DiffieHellmanKeyChangeContent content = new DiffieHellmanKeyChangeContent(publicKeyBytes);
        assertEquals(new String(publicKeyBytes), content.getStringMessage());
    }

    @Test
    public void testGetType() {
        byte[] publicKeyBytes = BigInteger.valueOf(123456).toByteArray();
        DiffieHellmanKeyChangeContent content = new DiffieHellmanKeyChangeContent(publicKeyBytes);
        assertEquals(ContentTypes.DIFFIE_HELLMAN, content.getType());
    }

    @Test
    public void testGetSubType() {
        byte[] publicKeyBytes = BigInteger.valueOf(123456).toByteArray();
        DiffieHellmanKeyChangeContent content = new DiffieHellmanKeyChangeContent(publicKeyBytes);
        assertEquals(DiffieHellmanTypes.KEY_CHANGE, content.getSubType());
    }

    @Test
    public void testGetDigest() {
        byte[] publicKeyBytes = BigInteger.valueOf(123456).toByteArray();
        DiffieHellmanKeyChangeContent content = new DiffieHellmanKeyChangeContent(publicKeyBytes);
        assertNotNull(content.getDigest());
    }

    @Test
    public void testHasValidDigest() {
        byte[] publicKeyBytes = BigInteger.valueOf(123456).toByteArray();
        DiffieHellmanKeyChangeContent content = new DiffieHellmanKeyChangeContent(publicKeyBytes);
        assertTrue(content.hasValidDigest());
    }
}
