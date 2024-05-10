package  Utils.Certificate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Date;

class PEMCertificateEncoderTest {

    private PEMCertificateEncoder encoder;
    private CustomCertificate certificate;

    @BeforeEach
    void setUp() throws Exception {
        encoder = new PEMCertificateEncoder();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        PublicKey publicKey = keyGen.generateKeyPair().getPublic();
        Date now = new Date();
        Date later = new Date(now.getTime() + 1000000);
        byte[] signature = new byte[]{1, 2, 3, 4, 5};

        certificate = new CertificateGenerator()
                .withPublicKey(publicKey)
                .issuedBy("Issuer")
                .forSubject("Subject")
                .validFrom(now)
                .validTo(later)
                .generate();
        certificate.setSignature(signature);
    }

    @Test
    void testEncode_ShouldEncodeCertificate() throws Exception {
        String encoded = encoder.encode(certificate);
        assertNotNull(encoded);
        assertTrue(encoded.contains("-----BEGIN CUSTOM CERTIFICATE-----"));
        assertTrue(encoded.contains("-----END CUSTOM CERTIFICATE-----"));
    }

    @Test
    void testDecode_ShouldDecodeCertificate() throws Exception {
        String encoded = encoder.encode(certificate);

        CustomCertificate decoded = encoder.decode(encoded);
        assertNotNull(decoded);
        assertEquals(certificate.getIssuer(), decoded.getIssuer());
        assertEquals(certificate.getSubject(), decoded.getSubject());
        assertEquals(certificate.getValidFrom(), decoded.getValidFrom());
        assertEquals(certificate.getValidTo(), decoded.getValidTo());
        assertArrayEquals(certificate.getSignature(), decoded.getSignature());
    }


    @Test
    void testEncodeNullCertificateThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            encoder.encode(null);
        });
        assertEquals("Certificate cannot be null.", exception.getMessage());
    }

    @Test
    void testDecodeNullDataThrowsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            encoder.decode(null);
        });
        assertEquals("Invalid PEM data provided.", exception.getMessage());
    }

    @Test
    void testDecodeInvalidPEMFormatThrowsIllegalArgumentException() {
        String invalidPEM = "INVALID PEM DATA";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            encoder.decode(invalidPEM);
        });
        assertEquals("Invalid PEM data provided.", exception.getMessage());
    }
}
