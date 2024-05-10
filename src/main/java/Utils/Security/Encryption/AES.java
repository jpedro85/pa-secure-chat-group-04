package Utils.Security.Encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for AES encryption and decryption.
 */
public class AES {

    private final static int BLOCK_SIZE = 16;
    private final static int ENCRYPTED_BLOCK_SIZE = 32;


    /**
     * Encrypts a single block using AES encryption with PKCS#5 padding.
     *
     * @param block   the block to be encrypted
     * @param secretKey the secret key used to encrypt the block
     * @return the encrypted block as an array of bytes
     * @throws Exception when the encryption fails
     */
    public static byte[] encryptAES ( byte[] block , byte[] secretKey ) throws Exception
    {

        byte[] secretKeyPadded = ByteBuffer.allocate ( 16 ).put ( secretKey ).array ( );
        SecretKeySpec secreteKeySpec = new SecretKeySpec ( secretKeyPadded , "AES" );
        Cipher cipher = Cipher.getInstance ( "AES/ECB/PKCS5Padding" );
        cipher.init ( Cipher.ENCRYPT_MODE , secreteKeySpec );
        return cipher.doFinal ( block );
    }

    /**
     * Decrypts a single block previously encrypted using AES encryption with PKCS#5 padding.
     *
     * @param block the block to be decrypted
     * @param secretKey the secret key used to decrypt the message
     * @return the decrypted message as an array of bytes
     * @throws Exception when the decryption fails
     */
    public static byte[] decryptAES ( byte[] block , byte[] secretKey ) throws Exception
    {
        byte[] secretKeyPadded = ByteBuffer.allocate ( 16 ).put ( secretKey ).array ( );
        SecretKeySpec secreteKeySpec = new SecretKeySpec ( secretKeyPadded , "AES" );
        Cipher cipher = Cipher.getInstance ( "AES/ECB/PKCS5Padding" );
        cipher.init ( Cipher.DECRYPT_MODE , secreteKeySpec );
        return cipher.doFinal ( block );
    }

    /**
     * Pads a message with PKCS#5 padding scheme to match the block size of AES encryption.
     *
     * @param message the message to be padded
     * @return the padded message
     */
    private static byte[] padMessage(byte[] message)
    {
        int paddingLength = BLOCK_SIZE - (message.length % BLOCK_SIZE);
        byte paddingByte = (byte)paddingLength;

        byte[] paddedMessage = new byte[message.length + paddingLength];

        System.arraycopy(message, 0, paddedMessage, 0, message.length);

        for (int i = message.length; i < paddedMessage.length; i++) {
            paddedMessage[i] = paddingByte;
        }
        return paddedMessage;
    }

    /**
     * Removes padding from a padded message.
     *
     * @param message the padded message
     * @return the dePadded message
     */
    private static byte[] dePadMessage(byte[] message)
    {
        byte padding = message[ message.length-1 ];
        int dePaddedLength = message.length - (int)padding;

        byte[] dePaddedMessage = new byte[dePaddedLength];
        System.arraycopy(message, 0, dePaddedMessage, 0, dePaddedLength);

        return dePaddedMessage;
    }

    /**
     * Encrypts a message string using AES encryption with PKCS#5 padding and divides it into blocks.
     *
     * @param msg       the message to be encrypted
     * @param secretKey the secret key used to encrypt the message
     * @return a list of encrypted message blocks
     * @throws Exception when the encryption fails
     */
    public static List<byte[]> encryptMessageToBlocks(String msg, byte[] secretKey)  throws Exception
    {
        byte[] paddedMessage = padMessage(msg.getBytes());

        List<byte[]> encryptedBlocks = new ArrayList<>();

        byte[] block = new byte[BLOCK_SIZE];
        for (int i = 0; i < paddedMessage.length; i += BLOCK_SIZE)
        {
            System.arraycopy(paddedMessage, i, block, 0, BLOCK_SIZE);
            byte[] encrypted = encryptAES( block, secretKey);
            encryptedBlocks.add( encrypted );
        }

        return encryptedBlocks;
    }

    /**
     * Encrypts a message string using AES encryption with PKCS#5 padding.
     *
     * @param msg       the message to be encrypted
     * @param secretKey the secret key used to encrypt the message
     * @return a byte[] of encrypted message
     * @throws Exception when the encryption fails
     */
    public static byte[] encryptMessage(String msg, byte[] secretKey)  throws Exception
    {
        List<byte[]> blockList = encryptMessageToBlocks(msg,secretKey);

        byte[] allMessage = new byte[ blockList.size() * ENCRYPTED_BLOCK_SIZE ];

        int actualPosition = 0;
        for( byte[] block : blockList )
        {
            System.arraycopy(block,0, allMessage, actualPosition , ENCRYPTED_BLOCK_SIZE );
            actualPosition += ENCRYPTED_BLOCK_SIZE;
        }

        return allMessage;
    }

    /**
     * Decrypts a message previously encrypted using AES encryption with PKCS#5 padding.
     *
     * @param paddedEncryptedMsg the padded encrypted message
     * @param secretKey           the secret key used to decrypt the message
     * @return the decrypted message
     * @throws Exception when the decryption fails
     */
    public static byte[] decryptMessage( byte[] paddedEncryptedMsg , byte[] secretKey) throws Exception
    {

        byte[] decryptedPaddedMessage = new byte[paddedEncryptedMsg.length / 2];

        byte[] block = new byte[ENCRYPTED_BLOCK_SIZE];
        int actualBlockSize = ENCRYPTED_BLOCK_SIZE;
        for (int i = 0 , j = 0; i < paddedEncryptedMsg.length; i += ENCRYPTED_BLOCK_SIZE , j += 16)
        {
            System.arraycopy(paddedEncryptedMsg, i, block, 0, ENCRYPTED_BLOCK_SIZE);
            byte[] decrypted = decryptAES( block, secretKey);
            actualBlockSize = decrypted.length;
            System.arraycopy( decrypted ,0 , decryptedPaddedMessage, j, actualBlockSize);
        }

        if ( actualBlockSize != BLOCK_SIZE )
            decryptedPaddedMessage[decryptedPaddedMessage.length-1] =  (byte)( BLOCK_SIZE - actualBlockSize);

        return dePadMessage(decryptedPaddedMessage);
    }

}
