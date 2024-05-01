package Utils.Message.Contents;

import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

public class RegisterContent implements MessageContent
{
    private final ContentSubtype type;
    private final String USERNAME;

    public RegisterContent( String userName ){
        this.USERNAME = userName;
        type = AccountMessageTypes.REGISTER;
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
}
