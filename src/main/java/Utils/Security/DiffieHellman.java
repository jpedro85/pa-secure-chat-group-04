package Utils.Security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class DiffieHellman {

    private static final int NUM_BITS = 128;
    private static final BigInteger N = new BigInteger ( "1289971646" );
    private static final BigInteger G = new BigInteger ( "3" );

    /**
     * Generates a private key to be used in the Diffie-Hellman key exchange.
     *
     * @return the private key
     *
     * @throws NoSuchAlgorithmException when the algorithm used to generate the key is not available
     */
    public static BigInteger generatePrivateKey ( ) throws NoSuchAlgorithmException {
        Random randomGenerator = SecureRandom.getInstance ( "SHA1PRNG" );
        return new BigInteger ( NUM_BITS , randomGenerator );
    }

    /**
     * Generates a public key to be used in the Diffie-Hellman key exchange.
     *
     * @param privateKey the private key
     *
     * @return the public key
     */
    public static BigInteger generatePublicKey ( BigInteger privateKey ) {
        return G.modPow ( privateKey , N );
    }

    /**
     * Generates a secret key to be used in the Diffie-Hellman key exchange.
     *
     * @param publicKey  the public key
     * @param privateKey the private key
     *
     * @return the secret key
     */
    public static BigInteger computeSecret ( BigInteger publicKey , BigInteger privateKey ) {
        return publicKey.modPow ( privateKey , N );
    }

}