package Utils.Security.Integrity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MACTest {

    // Test generating a MAC
    @Test
    void testGenerateMAC() {
        byte[] message = "Hello World".getBytes();
        byte[] macKey = "secretkey".getBytes();
        byte[] mac = MAC.generateMAC(message, macKey);

        assertNotNull(mac, "MAC should not be null");
        assertTrue(mac.length > 0, "MAC should not be empty");
    }

    // Test verifying a MAC
    @Test
    void testVerifyMAC() {
        byte[] message = "Hello World".getBytes();
        byte[] macKey = "secretkey".getBytes();
        byte[] mac = MAC.generateMAC(message, macKey);

        assertTrue(MAC.verifyMAC(mac, MAC.generateMAC(message, macKey)), "MAC verification should succeed");
    }

    // Test MAC generation and verification with different keys
    @Test
    void testMACMismatch() {
        byte[] message = "Hello World".getBytes();
        byte[] macKey1 = "secretkey".getBytes();
        byte[] macKey2 = "differentkey".getBytes();

        byte[] mac1 = MAC.generateMAC(message, macKey1);
        byte[] mac2 = MAC.generateMAC(message, macKey2);

        assertFalse(MAC.verifyMAC(mac1, mac2), "MAC verification should fail for different keys");
    }

}