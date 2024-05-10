package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.CommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Security.Integrity.HASH;

import java.lang.reflect.Type;

public class MessageCommunicationContent implements MessageContentIntegrityHash
{
    private final CommunicationTypes TYPE;

    private final byte[] MESSAGE;
    private final byte[] DIGEST;
    public MessageCommunicationContent( CommunicationTypes type , byte[] message )
    {
        TYPE = type;
        MESSAGE = message;
        DIGEST = HASH.generateDigest( getByteMessage() );
    }

    @Override
    public byte[] getByteMessage() {
        return MESSAGE ;
    }

    @Override
    public String getStringMessage() {
        return new String( MESSAGE) ;
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
    public boolean hasValidDigest()
    {
        return HASH.verifyDigest( HASH.generateDigest( getByteMessage() ) , DIGEST );
    }
}
