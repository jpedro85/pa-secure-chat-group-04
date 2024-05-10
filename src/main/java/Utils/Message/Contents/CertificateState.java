package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.CommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Security.Integrity.HASH;

public class CertificateState implements MessageContentIntegrityHash
{
    private final ContentSubtype TYPE;
    private final byte[] DIGEST;
    private final boolean IS_VALID;
    private final int SERIAL_NUMBER;

    public CertificateState( boolean isValid, int serialNumber)
    {
        this( isValid, serialNumber, CACommunicationTypes.CERTIFICATE_STATE );
    }

    protected CertificateState(boolean isValid, int serialNumber, ContentSubtype type)
    {
        this.TYPE = type;
        this.IS_VALID = isValid;
        this.SERIAL_NUMBER = serialNumber;
        this.DIGEST = HASH.generateDigest( getByteMessage() );
    }

    public boolean isValid()
    {
        return IS_VALID;
    }

    public int getSerialNumber()
    {
        return SERIAL_NUMBER;
    }
    @Override
    public byte[] getByteMessage() {
        return getStringMessage().getBytes();
    }

    @Override
    public String getStringMessage() {
        return "The certificate" + SERIAL_NUMBER + " is " +  IS_VALID ;
    }

    @Override
    public ContentTypes getType() {
        return TYPE.getSuperType();
    }

    @Override
    public ContentSubtype getSubType() {
        return TYPE;
    }

    @Override
    public byte[] getDigest() {
        return DIGEST;
    }

    @Override
    public boolean hasValidDigest() {
        return HASH.verifyDigest( HASH.generateDigest( getByteMessage() ), DIGEST );
    }
}
