package Utils.Message;

import Utils.Message.Contents.Interfaces.MessageContent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageTest {

    private MessageContent mockContent;
    private String expectedStringMessage = "Hello, World!";


    @BeforeEach
     void setUp() {
        mockContent = mock(MessageContent.class, withSettings().serializable());
        when(mockContent.getStringMessage()).thenReturn(expectedStringMessage);
    }

    @Test
    void testConstructorAndGetters() {
        Message message = new Message("Alice", "Bob", mockContent);

        assertEquals("Alice", message.getSender(), "Sender should match");
        assertEquals("Bob", message.getRecipient(), "Recipient should match");
        assertEquals(mockContent, message.getContent(), "Content should match");
    }

    @Test
    void testSerialization() throws IOException, ClassNotFoundException {
        Message originalMessage = new Message("Alice", "Bob", mockContent);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(originalMessage);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Message deserializedMessage = (Message) ois.readObject();

        MessageContent deserializedContent = mock(MessageContent.class, withSettings().serializable());
        when(deserializedContent.getStringMessage()).thenReturn(expectedStringMessage);

        assertEquals(originalMessage.getSender(), deserializedMessage.getSender());
        assertEquals(originalMessage.getRecipient(), deserializedMessage.getRecipient());
        assertEquals(deserializedContent.getStringMessage(), deserializedMessage.getContent().getStringMessage());
    }

}