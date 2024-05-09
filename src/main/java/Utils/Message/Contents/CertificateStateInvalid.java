package Utils.Message.Contents;

import Utils.Message.EnumTypes.CommunicationTypes;

public class CertificateStateInvalid extends CertificateState
{
    public CertificateStateInvalid( int serialNumber) {
        super(false, serialNumber, CommunicationTypes.INVALID_CERTIFICATE);
    }
}
