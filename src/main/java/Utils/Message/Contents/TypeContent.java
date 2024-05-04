package Utils.Message.Contents;

import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

public class TypeContent implements MessageContent
{
    private final ContentSubtype Type;
    public TypeContent( ContentSubtype type )
    {
        this.Type = type;
    }

    @Override
    public byte[] getByteMessage()
    {
        return getStringMessage().getBytes();
    }

    @Override
    public String getStringMessage()
    {
        return Type.toString();
    }

    @Override
    public ContentTypes getType()
    {
        return Type.getSuperType();
    }

    @Override
    public ContentSubtype getSubType()
    {
        return Type;
    }
}
