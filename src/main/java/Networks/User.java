package Networks;

import java.io.Serializable;

/**
 * Represents a user in a network. Used to store user data;
 */
public class User implements Serializable 
{
    /** The username of the user. */
    private final String username;

    /** The certificate associated with the user. */
    private String certificate;

    /**
     * Constructs a new User object with the given username.
     *
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the certificate associated with the user.
     *
     * @return The certificate associated with the user.
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * Sets the certificate associated with the user.
     *
     * @param certificate The certificate to set.
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public String toString() {
        return username + certificate;
    }
}
