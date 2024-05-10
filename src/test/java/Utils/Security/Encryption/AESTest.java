package Utils.Security.Encryption;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    @Test
    void testEncryptDecryptBlock() throws Exception {
        byte[] secretKey = "1234567890123456".getBytes(); // 16-byte key for AES
        byte[] data = "Hello, World!!!".getBytes(); // 16 bytes data

        byte[] encryptedData = AES.encryptAES(data, secretKey);
        byte[] decryptedData = AES.decryptAES(encryptedData, secretKey);

        assertArrayEquals(data, decryptedData, "Decrypted data should match original");
    }

    @Test
    void testEncryptDecryptMessage() throws Exception {
        byte[] secretKey = "1234567890123456".getBytes(); // 16-byte key for AES
        String message = "This is a longer message that spans multiple blocks!";

        byte[] encryptedMessage = AES.encryptMessage(message, secretKey);
        byte[] decryptedMessage = AES.decryptMessage(encryptedMessage, secretKey);

        assertArrayEquals(message.getBytes(), decryptedMessage, "Decrypted message should match the original message");
    }

}