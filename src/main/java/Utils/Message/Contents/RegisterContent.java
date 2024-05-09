package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Security.Integrity.HASH;

public class RegisterContent implements MessageContentIntegrityHash
{
    private final ContentSubtype type;
    private final byte[] DIGEST;
    private final String USERNAME;

    public RegisterContent( String userName ){
        this.USERNAME = userName;
        type = AccountMessageTypes.REGISTER;
        this.DIGEST = HASH.generateDigest( getByteMessage() );
    }

    @Override
    public byte[] getByteMessage() {
        return USERNAME.getBytes();
    }

    @Override
    public String getStringMessage() {
        return USERNAME;
    }

    @Override
    public ContentTypes getType() {
        return type.getSuperType();
    }

    @Override
    public ContentSubtype getSubType() {
        return type;
    }

    @Override
    public byte[] getDigest() {
        return DIGEST;
    }

    @Override
    public boolean hasValidDigest() {
        return HASH.verifyDigest( DIGEST, HASH.generateDigest(getByteMessage()) );
    }
}
