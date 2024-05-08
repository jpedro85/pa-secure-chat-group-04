package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Security.Integrity.HASH;

public class LogOutContent implements MessageContentIntegrityHash
{

    private final ContentSubtype TYPE;
    private final byte[] DIGEST;
    private final String USERNAME;


    public LogOutContent( String username )
    {
        TYPE = AccountMessageTypes.LOGOUT;
        USERNAME = username;
        DIGEST = HASH.generateDigest( getByteMessage() );
    }

    @Override
    public byte[] getByteMessage() {
        return getStringMessage().getBytes() ;
    }

    @Override
    public String getStringMessage() {
        return USERNAME;
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
        return HASH.verifyDigest( HASH.generateDigest( getByteMessage() ), DIGEST);
    }
}
