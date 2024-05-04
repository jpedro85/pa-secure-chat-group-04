package Utils.Message.Contents;

import Networks.User;
import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Security.Integrity.HASH;

import java.util.ArrayList;

public class AllLoggedInContent implements MessageContentIntegrityHash
{

    private final ContentSubtype TYPE;
    private final byte[] DIGEST;
    private final ArrayList<User> USERS ;

    public AllLoggedInContent( ArrayList<User> users )
    {
        if (users == null)
            throw  new IllegalArgumentException(" users can not be null");

        USERS = users;
        TYPE = AccountMessageTypes.LOGGED_USERS;
        DIGEST = HASH.generateDigest( getByteMessage() );
    }


    public ArrayList<User> getUsers()
    {
        return USERS;
    }

    @Override
    public byte[] getByteMessage() {
        return getStringMessage().getBytes() ;
    }

    @Override
    public String getStringMessage() {
        return USERS.toString();
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
        return HASH.verifyDigest( HASH.generateDigest( getByteMessage() ) , DIGEST );
    }
}
