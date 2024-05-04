package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContentIntegrityMAC;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Security.Integrity.MAC;

import java.math.BigInteger;
import java.security.PublicKey;

public class PublicKeyContent implements MessageContentIntegrityMAC
{
    private final ContentSubtype TYPE;
    private final byte[] MAC_CODE;
    private final PublicKey PUBLIC_KEY;


    public PublicKeyContent( PublicKey publicKey , BigInteger secret )
    {
        TYPE = CACommunicationTypes.PUBLIC_KEY;
        PUBLIC_KEY = publicKey;
        MAC_CODE = MAC.generateMAC( getByteMessage(), secret.toByteArray() );
    }

    public PublicKey getPublicKey()
    {
        return PUBLIC_KEY;
    }

    @Override
    public byte[] getByteMessage() {
        return getStringMessage().getBytes() ;
    }

    @Override
    public String getStringMessage() {
        return PUBLIC_KEY.toString();
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
    public byte[] getMAC() {
        return MAC_CODE;
    }

    @Override
    public boolean hasValidMAC(byte[] secret) {
        return MAC.verifyMAC( MAC.generateMAC( getByteMessage() , secret) , MAC_CODE);
    }
}
