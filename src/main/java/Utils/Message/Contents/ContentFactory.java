package Utils.Message.Contents;

import Utils.Message.EnumTypes.ContentSubtype;
import Utils.Security.CertificateChange;

public class ContentFactory
{
    private ContentFactory(){}

    public static  <Type extends ContentSubtype> MessageContent createTypeContent(Type type )
    {
        return new TypeContent<Type>( type );
    }

    public static MessageContent createErrorContent(MessageContent type , String error )
    {
        return new ErrorContent( type , error);
    }

    public static MessageContent createRegisterContent (String username )
    {
        return new RegisterContent( username );
    }
    public static MessageContent createLoginContent (CertificateChange certificate, String username)
    {
        return new LogInContent( certificate , username);
    }

}
