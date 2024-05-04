package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContentIntegrityMAC;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Security.Integrity.MAC;

import java.math.BigInteger;

public class IntegrityContent implements MessageContentIntegrityMAC
{
    private final ContentSubtype TYPE;
    private final String FILENAME;
    private final byte[] MAC_CODE;

    public IntegrityContent( String content , BigInteger secret , ContentSubtype type)
    {
        this.FILENAME = content;
        this.MAC_CODE = MAC.generateMAC( getByteMessage() , secret.toByteArray() );
        TYPE = type;
    }

    @Override
    public byte[] getByteMessage() {
        return getStringMessage().getBytes();
    }

    @Override
    public String getStringMessage() {
        return FILENAME;
    }

    @Override
    public ContentTypes getType() {
        return TYPE.getSuperType() ;
    }

    @Override
    public ContentSubtype getSubType() {
        return TYPE;
    }

    @Override
    public byte[] getMAC() {
        return MAC_CODE;
    }

    @Override
    public boolean hasValidMAC(byte[] secret) {
        return MAC.verifyMAC( MAC.generateMAC(  getByteMessage() , secret ) , MAC_CODE );
    }
}
