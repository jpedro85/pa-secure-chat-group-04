package Utils.Message.Contents;

import Networks.User;
import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.ArrayList;

public class ContentFactory
{
    private ContentFactory(){}

    public static  MessageContent createTypeContent( ContentSubtype type )
    {
        return new TypeContent( type );
    }

    public static  MessageContent createIntegrityContent( String content , BigInteger secret,  ContentSubtype type )
    {
        return new IntegrityContent( content, secret, type );
    }

    public static MessageContent createSigneContent( String Filename, BigInteger secret )
    {
        return new IntegrityContent( Filename, secret, CACommunicationTypes.SIGNE );
    }

    public static MessageContent createPublicKeyContent( PublicKey publicKey, BigInteger secret )
    {
        return new PublicKeyContent( publicKey, secret );
    }

    public static MessageContent createErrorContent(MessageContent content , String error )
    {
        return new ErrorContent( content , error);
    }

    public static MessageContent createRegisterContent (String username )
    {
        return new RegisterContent( username );
    }

    public static MessageContent createLoginContent (String certificate, String username)
    {
        return new LogInContent( certificate , username);
    }

    public static MessageContent createDiffieHellmanContent( BigInteger publicKey )
    {
        return new DiffieHellmanContent(publicKey);
    }

    public static MessageContent createAllLoggedInContent(ArrayList<User> users)
    {
        return new AllLoggedInContent( users );
    }

}
