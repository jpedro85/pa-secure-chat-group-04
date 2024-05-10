package Utils.Security;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class DiffieHellmanTest {

    @Test
    void testGeneratePrivateKey() {
        BigInteger privateKey1 = DiffieHellman.generatePrivateKey();
        BigInteger privateKey2 = DiffieHellman.generatePrivateKey();

        assertNotNull(privateKey1);
        assertNotNull(privateKey2);
        assertNotEquals(privateKey1, privateKey2, "Private keys must be unique and random");
    }

    @Test
    void testComputeSecret() {
        BigInteger privateKeyA = DiffieHellman.generatePrivateKey();
        BigInteger publicKeyA = DiffieHellman.generatePublicKey(privateKeyA);
        BigInteger privateKeyB = DiffieHellman.generatePrivateKey();
        BigInteger publicKeyB = DiffieHellman.generatePublicKey(privateKeyB);

        BigInteger secretA = DiffieHellman.computeSecret(publicKeyB, privateKeyA);
        BigInteger secretB = DiffieHellman.computeSecret(publicKeyA, privateKeyB);

        assertNotNull(secretA);
        assertNotNull(secretB);
        assertEquals(secretA, secretB, "Both parties should compute the same secret key");
    }

    @Test
    void testNoSuchAlgorithmExceptionHandled() {
        assertDoesNotThrow(DiffieHellman::generatePrivateKey,
                "No exception should be thrown when generating private key");
    }

}