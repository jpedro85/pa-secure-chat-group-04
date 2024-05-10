package Utils.Message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageRecordTest {

    @Test
    void testConstructorAndGetters() {
        MessageRecord record = new MessageRecord("Alice", "Hello, World!", "2024-05-10 10:00:00");

        assertEquals("Alice", record.getSender(), "Sender should match");
        assertEquals("Hello, World!", record.getMessage(), "Message should match");
        assertEquals("2024-05-10 10:00:00", record.getReceivedDate(), "Received date should match");
    }

    @Test
    void testToString() {
        MessageRecord record = new MessageRecord("Alice", "Hello, World!", "2024-05-10 10:00:00");
        String expectedOutput = "At:2024-05-10 10:00:00 Message from: Alice\nMessage: Hello, World!";

        assertEquals(expectedOutput, record.toString(), "toString output should be correctly formatted");
    }
}