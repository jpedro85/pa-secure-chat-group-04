package Utils.Security.Encryption;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class RSATest {

    @Test
    void testGenerateKeyPair() {
        KeyPair keyPair = RSA.generateKeyPair();

        assertNotNull(keyPair, "Key pair should not be null");
        assertNotNull(keyPair.getPrivate(), "Private key should not be null");
        assertNotNull(keyPair.getPublic(), "Public key should not be null");
    }

    @Test
    void testEncryptDecrypt() throws Exception {
        KeyPair keyPair = RSA.generateKeyPair();
        String originalText = "This is a test message.";
        byte[] message = originalText.getBytes();

        byte[] encryptedMessage = RSA.encryptRSA(message, keyPair.getPublic());
        byte[] decryptedMessage = RSA.decryptRSA(encryptedMessage, keyPair.getPrivate());

        assertArrayEquals(message, decryptedMessage, "Decrypted message should match the original");
    }

    @Test
    void testEncryptionWithNullKey() {
        String originalText = "This is a test message.";
        byte[] message = originalText.getBytes();

        assertThrows(RuntimeException.class, () -> RSA.encryptRSA(message, null),
                "Should throw a RuntimeException when encrypting with a null key");
    }

    @Test
    void testDecryptionWithNullKey() {
        byte[] message = "Encrypted message".getBytes();

        assertThrows(RuntimeException.class, () -> RSA.decryptRSA(message, null),
                "Should throw a RuntimeException when decrypting with a null key");
    }

}