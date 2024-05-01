package Utils.Message.Contents;

import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

public class RegisterContent implements MessageContent
{
    private final ContentSubtype type;
    private final String userName;

    public RegisterContent( String userName ){
        this.userName = userName;
        type = AccountMessageTypes.REGISTER;
    }

    @Override
    public byte[] getByteMessage() {
        return userName.getBytes();
    }

    @Override
    public String getStringMessage() {
        return userName;
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
