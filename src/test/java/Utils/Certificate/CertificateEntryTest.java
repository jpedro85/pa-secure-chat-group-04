package Utils.Certificate;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateEntryTest
{
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        CustomCertificate certificate = new CustomCertificate();

        // Act
        CertificateEntry entry = new CertificateEntry(certificate, true);

        // Assert
        assertEquals(certificate, entry.getCertificate());
        assertTrue(entry.isApproved());
    }

    @Test
    public void testRevoke() {
        // Arrange
        CustomCertificate certificate = new CustomCertificate();
        CertificateEntry entry = new CertificateEntry(certificate, true);

        // Act
        entry.revoke();

        // Assert
        assertFalse(entry.isApproved());
    }
}
