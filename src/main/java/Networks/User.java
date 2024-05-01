package Networks;

public class User {

    private String username;

    private String certificate;

    public User(String username) {
        username = username;
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
}
