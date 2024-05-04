package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

public class LogInContent implements MessageContent
{

    private final ContentSubtype TYPE;
    private final String CERTIFICATE;

    private final String USERNAME;

    public LogInContent( String certificate, String username )
    {
        this.CERTIFICATE = certificate;
        this.TYPE = AccountMessageTypes.LOGIN;
        this.USERNAME = username;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getCertificate() {
        return CERTIFICATE;
    }

    @Override
    public byte[] getByteMessage()
    {
        return new byte[0];
    }

    @Override
    public String getStringMessage()
    {
        return new String() /*TODO:implete rest */ ;
    }

    @Override
    public ContentTypes getType()
    {
        return TYPE.getSuperType();
    }

    @Override
    public ContentSubtype getSubType()
    {
        return TYPE;
    }
}
