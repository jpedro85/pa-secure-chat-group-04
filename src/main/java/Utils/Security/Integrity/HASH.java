package Utils.Security.Integrity;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * This class implements the generation and verification of the message digest.
 */
public class HASH {

    private static final String DIGEST_ALGORITHM = "SHA-512";

    /**
     * Computes the message digest of the given message.
     *
     * @param message The message to be digested.
     *
     * @return the message digest
     */
    public static byte[] generateDigest ( byte[] message )
    {
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            return messageDigest.digest(message);
        }
        catch ( Exception e )
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies the message digest of the given message.
     *
     * @param digest         the message digest to be verified
     * @param computedDigest the computed message digest
     *
     * @return true if the message digest is valid, false otherwise
     */
    public static boolean verifyDigest ( byte[] digest , byte[] computedDigest ) {
        return Arrays.equals ( digest , computedDigest );
    }

}
