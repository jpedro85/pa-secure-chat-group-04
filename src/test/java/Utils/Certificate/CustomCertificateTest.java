package Utils.Certificate;

import org.junit.jupiter.api.*;

import java.security.PublicKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CustomCertificateTest {

    CustomCertificate certificate;
    PublicKey publicKeyMock;
    Date validFrom;
    Date validTo;
    byte[] signature;

    @BeforeEach
    void setUp() {
        publicKeyMock = mock(PublicKey.class);
        validFrom = new Date(System.currentTimeMillis() - 1000); // 1 second ago
        validTo = new Date(System.currentTimeMillis() + 1000); // 1 second ahead
        signature = new byte[] {1, 2, 3, 4, 5};

        certificate = new CustomCertificate();
        certificate.setPublicKey(publicKeyMock);
        certificate.setIssuer("Test Issuer");
        certificate.setSubject("Test Subject");
        certificate.setValidFrom(validFrom);
        certificate.setValidTo(validTo);
        certificate.setSignature(signature);
    }

    @Test
    void testGetIssuer() {
        assertEquals("Test Issuer", certificate.getIssuer(), "Issuer should be 'Test Issuer'");
    }

    @Test
    void testGetSubject() {
        assertEquals("Test Subject", certificate.getSubject(), "Subject should be 'Test Subject'");
    }

    @Test
    void testGetValidFromDate() {
        assertEquals(validFrom, certificate.getValidFrom(), "Valid from date should match");
    }

    @Test
    void testGetValidToDate() {
        assertEquals(validTo, certificate.getValidTo(), "Valid to date should match");
    }

    @Test
    void testIsValidWhenCurrentDateIsWithinRange() {
        assertTrue(certificate.isValid(), "Certificate should be valid when the current date is within the valid range");
    }

    @Test
    void testIsValidWhenCurrentDateIsOutOfRange() {
        // Setting date out of range
        certificate.setValidFrom(new Date(System.currentTimeMillis() + 2000)); // 2 seconds ahead
        certificate.setValidTo(new Date(System.currentTimeMillis() + 3000)); // 3 seconds ahead

        assertFalse(certificate.isValid(), "Certificate should not be valid when the current date is out of the valid range");
    }
}