package Utils.Message.Contents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.ContentTypes;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateStateTest {

    @Test
    @DisplayName("testIsValid")
    public void testIsValid() {
        boolean isValid = true;
        CertificateState certificateState = new CertificateState(isValid, 123456);
        assertEquals(isValid, certificateState.isValid());
    }

    @Test
    @DisplayName("testGetSerialNumber")
    public void testGetSerialNumber() {
        int serialNumber = 123456;
        CertificateState certificateState = new CertificateState(true, serialNumber);
        assertEquals(serialNumber, certificateState.getSerialNumber());
    }

    @Test
    @DisplayName("testGetStringMessage")
    public void testGetStringMessage() {
        boolean isValid = true;
        int serialNumber = 123456;
        CertificateState certificateState = new CertificateState(isValid, serialNumber);
        assertEquals("The certificate" + serialNumber + " is " + isValid, certificateState.getStringMessage());
    }

    @Test
    @DisplayName("testGetType")
    public void testGetType() {
        CertificateState certificateState = new CertificateState(true, 123456);
        assertEquals(ContentTypes.CA_COMMUNICATION, certificateState.getType());
    }

    @Test
    @DisplayName("testGetSubType")
    public void testGetSubType() {
        CertificateState certificateState = new CertificateState(true, 123456);
        assertEquals(CACommunicationTypes.CERTIFICATE_STATE, certificateState.getSubType());
    }

    @Test
    public void testHasValidDigest() {
        boolean isValid = true;
        int serialNumber = 123456;
        CertificateState certificateState = new CertificateState(isValid, serialNumber);
        assertTrue(certificateState.hasValidDigest());
    }
}
