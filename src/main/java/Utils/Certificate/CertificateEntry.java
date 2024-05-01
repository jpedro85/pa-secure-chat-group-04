package Utils.Certificate;

/**
 * The CertificateEntry class represents an entry in the certificate database.
 * It stores the name of the certificate and its approval status.
 */
public class CertificateEntry {
    
    private String name;
    private boolean approved;

    /**
     * Constructs a CertificateEntry object with the specified name and approval status.
     *
     * @param name     The name of the certificate.
     * @param approved The approval status of the certificate.
     */
    public CertificateEntry(String name, boolean approved) {
        this.name = name;
        this.approved = approved;
    }

    /**
     * Gets the name of the certificate.
     *
     * @return The name of the certificate.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the certificate is approved.
     *
     * @return true if the certificate is approved, false otherwise.
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Returns a string representation of the CertificateEntry object.
     *
     * @return A string representation of the CertificateEntry object.
     */
    @Override
    public String toString() {
        return "CertificateEntry{" +
                "name='" + name + '\'' +
                ", approved=" + approved +
                '}';
    }
}
