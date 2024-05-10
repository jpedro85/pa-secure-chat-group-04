package Networks;

import Utils.Concurrency.VarSync;

import java.math.BigInteger;

public class ClientUser extends User
{
    private VarSync<BigInteger> sharedSecret = new VarSync<>(null);

    private BigInteger generatedPrivateKey;

    private BigInteger generatedPublicKey;

    private boolean isAgreeingOnSecret = false;

    public ClientUser(String username) {
        super(username);
    }

    public BigInteger getSharedSecret() {
        return sharedSecret.syncGet();
    }

    public void setSharedSecret(BigInteger sharedSecret)
    {
        this.sharedSecret.syncSet(sharedSecret);
    }

    public BigInteger getGeneratedPrivateKey() {
        return generatedPrivateKey;
    }

    public void setGeneratedPrivateKey(BigInteger generatedPrivateKey)
    {
        this.generatedPrivateKey = generatedPrivateKey;
    }

    public BigInteger getGeneratedPublicKey() {
        return generatedPublicKey;
    }

    public void setGeneratedPublicKey(BigInteger generatedPublicKey)
    {
        this.generatedPublicKey = generatedPublicKey;
    }

    public boolean isAgreeingOnSecret() {
        return isAgreeingOnSecret;
    }

    public void setAgreeingOnSecret(boolean agreeingOnSecret) {
        isAgreeingOnSecret = agreeingOnSecret;
    }

    public boolean hasAgreedOnSecret()
    {
        return sharedSecret.syncGet() != null;
    }
}
