package Networks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ClientUserTest
{
    @Test
    @DisplayName("Test getters and setters")
    public void testGettersAndSetters() {
        ClientUser clientUser = new ClientUser("Alice");

        // Test getUsername method
        assertEquals("Alice", clientUser.getUsername());

        // Test setGeneratedPrivateKey and getGeneratedPrivateKey methods
        BigInteger privateKey = new BigInteger("1234567890");
        clientUser.setGeneratedPrivateKey(privateKey);
        assertEquals(privateKey, clientUser.getGeneratedPrivateKey());

        // Test setGeneratedPublicKey and getGeneratedPublicKey methods
        BigInteger publicKey = new BigInteger("9876543210");
        clientUser.setGeneratedPublicKey(publicKey);
        assertEquals(publicKey, clientUser.getGeneratedPublicKey());

        // Test setSharedSecret and getSharedSecret methods
        BigInteger sharedSecret = new BigInteger("246813579");
        clientUser.setSharedSecret(sharedSecret);
        assertEquals(sharedSecret, clientUser.getSharedSecret());

        // Test isAgreeingOnSecret and setAgreeingOnSecret methods
        assertFalse(clientUser.isAgreeingOnSecret());
        clientUser.setAgreeingOnSecret(true);
        assertTrue(clientUser.isAgreeingOnSecret());

        // Test hasAgreedOnSecret method
        assertTrue(clientUser.hasAgreedOnSecret());
    }
}
