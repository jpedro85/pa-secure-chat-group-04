package Networks;

import java.io.Serializable;

public class User implements Serializable 
{
    private final String username;

    private String certificate;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public String toString() {
        return username + certificate;
    }
}
