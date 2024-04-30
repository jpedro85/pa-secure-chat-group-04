package Networks;

public class CertificateEntry {
    
    private String name;
    private boolean approved;

    public CertificateEntry(String name, boolean approved) {
        this.name = name;
        this.approved = approved;
    }

    public String getName() {
        return name;
    }

    public boolean isApproved() {
        return approved;
    }

    @Override
    public String toString() {
        return "CertificateEntry{" +
                "name='" + name + '\'' +
                ", approved=" + approved +
                '}';
    }
}
