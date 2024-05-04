package Utils.Certificate;

/**
 * The CertificateEntry class represents an entry in the certificate database.
 * It stores the name of the certificate and its approval status.
 */
public class CertificateEntry {
    
    private CustomCertificate CERTIFICATE;
    private boolean approved ;

    /**
     * Constructs a CertificateEntry object with the specified name and approval status.
     *
     * @param certificate The Certificate
     * @param approved The approval status of the certificate.
     */
    public CertificateEntry(CustomCertificate certificate, boolean approved)
    {
        this.CERTIFICATE = certificate;
        this.approved = approved;
    }

    /**
     * Gets the name of the certificate.
     *
     * @return The name of the certificate.
     */
    public CustomCertificate getCertificate()
    {
        return CERTIFICATE;
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
     * Revokes the certificate. This change can not be undone;
     */
    public void revoke() {
        approved = false;
    }

}
