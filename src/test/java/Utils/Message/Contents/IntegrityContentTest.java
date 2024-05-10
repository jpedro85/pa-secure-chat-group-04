package Utils.Message.Contents;

import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigInteger;

public class IntegrityContentTest {

    @Test
    public void testGetByteMessage() {
        String content = "testContent";
        BigInteger secret = BigInteger.valueOf(123456);
        ContentSubtype type = CACommunicationTypes.SIGNE;
        IntegrityContent integrityContent = new IntegrityContent(content, secret, type);
        assertEquals(content.getBytes(), integrityContent.getByteMessage());
    }

    @Test
    public void testGetStringMessage() {
        String content = "testContent";
        BigInteger secret = BigInteger.valueOf(123456);
        ContentSubtype type = CACommunicationTypes.SIGNE;
        IntegrityContent integrityContent = new IntegrityContent(content, secret, type);
        assertEquals(content, integrityContent.getStringMessage());
    }

    @Test
    public void testGetType() {
        String content = "testContent";
        BigInteger secret = BigInteger.valueOf(123456);
        ContentSubtype type = CACommunicationTypes.SIGNE;
        IntegrityContent integrityContent = new IntegrityContent(content, secret, type);
        assertEquals(ContentTypes.CA_COMMUNICATION, integrityContent.getType());
    }

    @Test
    public void testGetSubType() {
        String content = "testContent";
        BigInteger secret = BigInteger.valueOf(123456);
        ContentSubtype type = CACommunicationTypes.SIGNE;
        IntegrityContent integrityContent = new IntegrityContent(content, secret, type);
        assertEquals(type, integrityContent.getSubType());
    }

    @Test
    public void testGetMAC() {
        String content = "testContent";
        BigInteger secret = BigInteger.valueOf(123456);
        ContentSubtype type = CACommunicationTypes.SIGNE;
        IntegrityContent integrityContent = new IntegrityContent(content, secret, type);
        assertNotNull(integrityContent.getMAC());
    }

    @Test
    public void testHasValidMAC() {
        String content = "testContent";
        BigInteger secret = BigInteger.valueOf(123456);
        ContentSubtype type = CACommunicationTypes.SIGNE;
        IntegrityContent integrityContent = new IntegrityContent(content, secret, type);
        assertTrue(integrityContent.hasValidMAC(secret.toByteArray()));
    }
}
