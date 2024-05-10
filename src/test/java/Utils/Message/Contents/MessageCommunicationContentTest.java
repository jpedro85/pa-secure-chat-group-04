package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.CommunicationTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;

public class MessageCommunicationContentTest {

    @Test
    public void testGetByteMessage() {
        byte[] message = "testMessage".getBytes();
        CommunicationTypes type = CommunicationTypes.MSG;
        MessageCommunicationContent content = new MessageCommunicationContent(type, message);
        assertArrayEquals(message, content.getByteMessage());
    }

    @Test
    public void testGetStringMessage() {
        byte[] message = "testMessage".getBytes();
        CommunicationTypes type = CommunicationTypes.MSG;
        MessageCommunicationContent content = new MessageCommunicationContent(type, message);
        assertEquals(new String(message), content.getStringMessage());
    }

    @Test
    public void testGetType() {
        byte[] message = "testMessage".getBytes();
        CommunicationTypes type = CommunicationTypes.MSG;
        MessageCommunicationContent content = new MessageCommunicationContent(type, message);
        assertEquals(ContentTypes.COMMUNICATION, content.getType());
    }

    @Test
    public void testGetSubType() {
        byte[] message = "testMessage".getBytes();
        CommunicationTypes type = CommunicationTypes.MSG;
        MessageCommunicationContent content = new MessageCommunicationContent(type, message);
        assertEquals(type, content.getSubType());
    }

    @Test
    public void testGetDigest() {
        byte[] message = "testMessage".getBytes();
        CommunicationTypes type = CommunicationTypes.MSG;
        MessageCommunicationContent content = new MessageCommunicationContent(type, message);
        assertNotNull(content.getDigest());
    }

    @Test
    public void testHasValidDigest() {
        byte[] message = "testMessage".getBytes();
        CommunicationTypes type = CommunicationTypes.MSG;
        MessageCommunicationContent content = new MessageCommunicationContent(type, message);
        assertTrue(content.hasValidDigest());
    }
}

