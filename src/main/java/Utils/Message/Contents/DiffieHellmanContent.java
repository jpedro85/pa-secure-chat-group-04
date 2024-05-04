package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.DiffieHellmanTypes;
import Utils.Security.Integrity.HASH;

import java.math.BigInteger;

public class DiffieHellmanContent implements MessageContentIntegrityHash
{

    private final ContentSubtype TYPE;
    private final BigInteger PUBLIC_KEY;
    private final byte[] DIGEST;

    public DiffieHellmanContent(BigInteger publicKey )
    {
        TYPE = DiffieHellmanTypes.KEY_CHANGE;
        PUBLIC_KEY = publicKey;
        DIGEST = HASH.generateDigest( getByteMessage() );
    }

    public BigInteger getPublic_key() {
        return PUBLIC_KEY;
    }

    @Override
    public byte[] getByteMessage() {
        return getStringMessage().getBytes() ;
    }

    @Override
    public String getStringMessage() {
        return PUBLIC_KEY.toString() ;
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
        return HASH.verifyDigest( DIGEST , HASH.generateDigest( getByteMessage() ));
    }
}
