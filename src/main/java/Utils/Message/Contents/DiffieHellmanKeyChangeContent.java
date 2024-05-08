package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.DiffieHellmanTypes;
import Utils.Security.Integrity.HASH;

import java.math.BigInteger;

public class DiffieHellmanKeyChangeContent implements MessageContentIntegrityHash
{

    private final ContentSubtype TYPE;
    private final byte[] PUBLIC_KEY;
    private final byte[] DIGEST;

    public DiffieHellmanKeyChangeContent( byte[] publicKey )
    {
        TYPE = DiffieHellmanTypes.KEY_CHANGE;
        PUBLIC_KEY = publicKey;
        DIGEST = HASH.generateDigest( getByteMessage() );
    }

    public BigInteger getPublic_key() {
        return new BigInteger(PUBLIC_KEY);
    }

    @Override
    public byte[] getByteMessage() {
        return PUBLIC_KEY ;
    }

    @Override
    public String getStringMessage() { return new String( PUBLIC_KEY ); }

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
