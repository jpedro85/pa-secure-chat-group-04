package Utils.Security.Integrity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class implements the generation and verification of the message MAC.
 */
public class MAC {

    private static final String MAC_ALGORITHM = "HmacSHA256";

    /**
     * Computes the message MAC of the given message.
     *
     * @param message The message to be digested.
     * @param macKey  the secret key for the MAC algorithm
     *
     * @return the message MAC
     *
     */
    public static byte[] generateMAC ( byte[] message , byte[] macKey )
    {
        try
        {
            SecretKeySpec secretKeySpec = new SecretKeySpec ( macKey , MAC_ALGORITHM );
            Mac mac = Mac.getInstance ( MAC_ALGORITHM );
            mac.init ( secretKeySpec );
            return mac.doFinal ( message );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Verifies the message authentication code (MAC) of the message.
     *
     * @param mac         the message authentication code
     * @param computedMac the computed message authentication code
     *
     * @return true if the message authentication codes are equal, false otherwise
     */
    public static boolean verifyMAC ( byte[] mac , byte[] computedMac ) {
        return Arrays.equals ( mac , computedMac );
    }

}
