package Utils.Certificate;

import java.io.Serializable;
import java.security.*;
import java.util.Date;

/**
 * Represents a custom certificate object used for various security purposes.
 * This class allows for the creation and manipulation of certificate data.
 */
public class CustomCertificate implements Serializable {

    private PublicKey publicKey;                // The public key associated with the certificate
    private String issuer;                      // The entity that issued the certificate
    private String subject;                     // The subject of the certificate
    private Date validFrom;                     // The start date of certificate validity
    private Date validTo;                       // The end date of certificate validity
    private int serialNumber;                   // The unique serial number of the certificate
    private byte[] signature;                   // The digital signature of the certificate
    private static int serialNumberCount = 0;   // Counter for generating serial numbers

    /**
     * Constructs a new CustomCertificate object with a unique serial number.
     */
    public CustomCertificate() {
        this.serialNumber = serialNumberCount;
        serialNumberCount++;
    }

    /**
     * Sets the issuer of the certificate.
     *
     * @param issuer The entity that issued the certificate.
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * Sets the subject of the certificate.
     *
     * @param subject The subject of the certificate.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Sets the validity end date of the certificate.
     *
     * @param validTo The end date of certificate validity.
     */
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    /**
     * Sets the public key associated with the certificate.
     *
     * @param publicKey The public key associated with the certificate.
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Retrieves the public key associated with the certificate.
     *
     * @return The public key associated with the certificate.
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the validity start date of the certificate.
     *
     * @param validFrom The start date of certificate validity.
     */
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * Sets the digital signature of the certificate.
     *
     * @param signature The digital signature of the certificate.
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * Retrieves the digital signature of the certificate.
     *
     * @return The digital signature of the certificate.
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Generates and retrieves the concatenated data of the certificate.
     * This data is used for creating the digital signature.
     *
     * @return The concatenated data of the certificate.
     */
    private byte[] getCertificateData() {
        return (serialNumber + issuer + subject + validFrom.toString() + validTo.toString() + publicKey.toString())
                .getBytes();
    }

}
