package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Security.Integrity.HASH;

public class LogInContent implements MessageContentIntegrityHash
{

    private final ContentSubtype TYPE;

    private final byte[] DIGEST;
    private final String CERTIFICATE;

    private final String USERNAME;

    public LogInContent( String certificate, String username )
    {
        this( certificate , username , AccountMessageTypes.LOGIN );
    }

    protected LogInContent( String certificate, String username, AccountMessageTypes type)
    {
        this.CERTIFICATE = certificate;
        this.TYPE = type;
        this.USERNAME = username;
        this.DIGEST = HASH.generateDigest(getByteMessage());
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getCertificate() {
        return CERTIFICATE;
    }

    @Override
    public byte[] getByteMessage()
    {
        return getStringMessage().getBytes();
    }

    @Override
    public String getStringMessage()
    {
        return USERNAME + CERTIFICATE;
    }

    @Override
    public ContentTypes getType()
    {
        return TYPE.getSuperType();
    }

    @Override
    public ContentSubtype getSubType()
    {
        return TYPE;
    }

    @Override
    public byte[] getDigest() {
        return DIGEST;
    }

    @Override
    public boolean hasValidDigest() {
        return HASH.verifyDigest( HASH.generateDigest( getByteMessage() ) ,DIGEST);
    }
}
