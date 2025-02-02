package Utils.Security.Encryption;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Class contains function to encrypt, decrypt and generate keypair for RSA algorithm.
 */
public class RSA
{

    /**
     * Generates a RSA keypair with size 2048.
     * @return the generated Keypair.
     */
    public static KeyPair generateKeyPair()
    {
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Encrypts a message using RSA encryption.
     *
     * @param message   the message to be encrypted
     * @param publicKey the public key to be used for encryption
     *
     * @return the encrypted message
     *
     */
    public static byte[] encryptRSA ( byte[] message , Key publicKey )
    {
        try
        {
            Cipher cipher = Cipher.getInstance ( "RSA" );
            cipher.init ( Cipher.ENCRYPT_MODE , publicKey );
            return cipher.doFinal ( message );
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Decrypts a message using RSA decryption.
     *
     * @param message    the message to be decrypted
     * @param privateKey the private key to be used for decryption
     *
     * @return the decrypted message
     *
     */
    public static byte[] decryptRSA ( byte[] message , Key privateKey )
    {
        try
        {
            Cipher cipher = Cipher.getInstance ( "RSA" );
            cipher.init ( Cipher.DECRYPT_MODE , privateKey );
            return cipher.doFinal ( message );
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }
    }

}
