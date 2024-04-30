package Utils.Security.Encryption;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSA
{

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * Encrypts a message using RSA encryption.
     *
     * @param message   the message to be encrypted
     * @param publicKey the public key to be used for encryption
     *
     * @return the encrypted message
     *
     * @throws Exception if any error occurs during the encryption process
     */
    public static byte[] encryptRSA ( byte[] message , Key publicKey ) throws Exception
    {
        Cipher cipher = Cipher.getInstance ( "RSA" );
        cipher.init ( Cipher.ENCRYPT_MODE , publicKey );
        return cipher.doFinal ( message );
    }

    /**
     * Decrypts a message using RSA decryption.
     *
     * @param message    the message to be decrypted
     * @param privateKey the private key to be used for decryption
     *
     * @return the decrypted message
     *
     * @throws Exception if any error occurs during the decryption process
     */
    public static byte[] decryptRSA ( byte[] message , Key privateKey ) throws Exception
    {
        Cipher cipher = Cipher.getInstance ( "RSA" );
        cipher.init ( Cipher.DECRYPT_MODE , privateKey );
        return cipher.doFinal ( message );
    }

}
