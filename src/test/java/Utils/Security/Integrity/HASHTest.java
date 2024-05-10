package Utils.Security.Integrity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HASHTest {

    @Test
    void testGenerateDigest() {
        byte[] message = "Hello World".getBytes();
        byte[] digest = HASH.generateDigest(message);

        assertNotNull(digest, "Digest should not be null");
        assertTrue(digest.length > 0, "Digest should not be empty");
    }

    // Test verifying a digest
    @Test
    void testVerifyDigest() {
        byte[] message = "Hello World".getBytes();
        byte[] digest = HASH.generateDigest(message);
        assertTrue(HASH.verifyDigest(digest, HASH.generateDigest(message)), "Digest verification should succeed");
    }

    // Test digest generation and verification with different messages
    @Test
    void testDigestMismatch() {
        byte[] message1 = "Hello World".getBytes();
        byte[] message2 = "Goodbye World".getBytes();
        assertFalse(HASH.verifyDigest(HASH.generateDigest(message1), HASH.generateDigest(message2)), "Digest verification should fail for different messages");
    }
}