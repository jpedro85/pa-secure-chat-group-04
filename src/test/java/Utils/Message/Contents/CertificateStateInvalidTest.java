package Utils.Message.Contents;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.CommunicationTypes;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateStateInvalidTest {

    @Test
    public void testIsValid() {
        int serialNumber = 123456;
        CertificateStateInvalid certificateStateInvalid = new CertificateStateInvalid(serialNumber);
        assertFalse(certificateStateInvalid.isValid());
    }

    @Test
    public void testGetSerialNumber() {
        int serialNumber = 123456;
        CertificateStateInvalid certificateStateInvalid = new CertificateStateInvalid(serialNumber);
        assertEquals(serialNumber, certificateStateInvalid.getSerialNumber());
    }

    @Test
    public void testGetStringMessage() {
        int serialNumber = 123456;
        CertificateStateInvalid certificateStateInvalid = new CertificateStateInvalid(serialNumber);
        assertEquals("The certificate" + serialNumber + " is false", certificateStateInvalid.getStringMessage());
    }

    @Test
    public void testGetType() {
        CertificateStateInvalid certificateStateInvalid = new CertificateStateInvalid(123456);
        assertEquals(ContentTypes.CA_COMMUNICATION, certificateStateInvalid.getType());
    }

    @Test
    public void testGetSubType() {
        CertificateStateInvalid certificateStateInvalid = new CertificateStateInvalid(123456);
        assertEquals(CommunicationTypes.INVALID_CERTIFICATE, certificateStateInvalid.getSubType());
    }

    @Test
    public void testHasValidDigest() {
        int serialNumber = 123456;
        CertificateStateInvalid certificateStateInvalid = new CertificateStateInvalid(serialNumber);
        assertTrue(certificateStateInvalid.hasValidDigest());
    }
} 

