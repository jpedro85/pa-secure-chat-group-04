package Utils.Certificate;

import java.security.PublicKey;
import java.util.Date;

/**
 * Utility class for generating custom certificates with ease.
 * This class provides a fluent API for setting various properties of the
 * certificate.
 */
public class CertificateGenerator {

    private CustomCertificate customCertificate; // The custom certificate being generated

    /**
     * Constructs a new CertificateGenerator and initializes a CustomCertificate
     * object.
     */
    public CertificateGenerator() {
        customCertificate = new CustomCertificate();
    }

    /**
     * Sets the subject of the certificate.
     *
     * @param subject The subject of the certificate.
     * @return The CertificateGenerator instance for method chaining.
     */
    public CertificateGenerator forSubject(String subject) {
        customCertificate.setSubject(subject);
        return this;
    }

    /**
     * Sets the issuer of the certificate.
     *
     * @param issuer The entity that issued the certificate.
     * @return The CertificateGenerator instance for method chaining.
     */
    public CertificateGenerator issuedBy(String issuer) {
        customCertificate.setIssuer(issuer);
        return this;
    }

    /**
     * Sets the validity start date of the certificate.
     *
     * @param validFrom The start date of certificate validity.
     * @return The CertificateGenerator instance for method chaining.
     */
    public CertificateGenerator validFrom(Date validFrom) {
        customCertificate.setValidFrom(validFrom);
        return this;
    }

    /**
     * Sets the validity end date of the certificate.
     *
     * @param validTo The end date of certificate validity.
     * @return The CertificateGenerator instance for method chaining.
     */
    public CertificateGenerator validTo(Date validTo) {
        customCertificate.setValidTo(validTo);
        return this;
    }

    /**
     * Sets the public key associated with the certificate.
     *
     * @param publicKey The public key associated with the certificate.
     * @return The CertificateGenerator instance for method chaining.
     */
    public CertificateGenerator withPublicKey(PublicKey publicKey) {
        customCertificate.setPublicKey(publicKey);
        return this;
    }

    /**
     * Generates and retrieves the custom certificate with the configured
     * properties.
     *
     * @return The generated CustomCertificate object.
     */
    public CustomCertificate generate() {
        return customCertificate;
    }

}
