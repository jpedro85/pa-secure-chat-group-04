package Networks;

import Utils.Concurrency.VarSync;

import java.math.BigInteger;

/**
 * Represents a client user in a network, extending the User class.
 */
public class ClientUser extends User
{
    /** Variable to synchronize the shared secret. */
    private VarSync<BigInteger> sharedSecret = new VarSync<>(null);

    /** The private key generated by the client. */
    private BigInteger generatedPrivateKey;

    /** The public key generated by the client. */
    private BigInteger generatedPublicKey;

    /** Flag indicating whether the client is currently agreeing on a secret. */
    private boolean isAgreeingOnSecret = false;

    /**
     * Constructs a new ClientUser object with the given username.
     *
     * @param username The username of the client user.
     */
    public ClientUser(String username) {
        super(username);
    }

    /**
     * Gets the shared secret agreed upon by the client.
     *
     * @return The shared secret.
     */
    public BigInteger getSharedSecret() {
        return sharedSecret.syncGet();
    }

    /**
     * Sets the shared secret agreed upon by the client.
     *
     * @param sharedSecret The shared secret to set.
     */
    public void setSharedSecret(BigInteger sharedSecret)
    {
        this.sharedSecret.syncSet(sharedSecret);
    }

    /**
     * Gets the private key generated by the client.
     *
     * @return The generated private key.
     */
    public BigInteger getGeneratedPrivateKey() {
        return generatedPrivateKey;
    }

    /**
     * Sets the private key generated by the client.
     *
     * @param generatedPrivateKey The generated private key to set.
     */
    public void setGeneratedPrivateKey(BigInteger generatedPrivateKey)
    {
        this.generatedPrivateKey = generatedPrivateKey;
    }

    /**
     * Gets the public key generated by the client.
     *
     * @return The generated public key.
     */
    public BigInteger getGeneratedPublicKey() {
        return generatedPublicKey;
    }

    /**
     * Sets the public key generated by the client.
     *
     * @param generatedPublicKey The generated public key to set.
     */
    public void setGeneratedPublicKey(BigInteger generatedPublicKey)
    {
        this.generatedPublicKey = generatedPublicKey;
    }

    /**
     * Checks if the client is currently agreeing on a secret.
     *
     * @return True if the client is agreeing on a secret, false otherwise.
     */
    public boolean isAgreeingOnSecret() {
        return isAgreeingOnSecret;
    }

    /**
     * Sets the flag indicating whether the client is agreeing on a secret.
     *
     * @param agreeingOnSecret The value to set for the flag.
     */
    public void setAgreeingOnSecret(boolean agreeingOnSecret) {
        isAgreeingOnSecret = agreeingOnSecret;
    }

    /**
     * Checks if the client has already agreed upon a secret.
     *
     * @return True if the client has agreed upon a secret, false otherwise.
     */
    public boolean hasAgreedOnSecret()
    {
        return sharedSecret.syncGet() != null;
    }
}
