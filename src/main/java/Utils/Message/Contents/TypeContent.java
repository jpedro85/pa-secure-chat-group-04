package Utils.Message.Contents;

import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

public class TypeContent< Type extends ContentSubtype> implements MessageContent
{
    private final Type Type;
    public TypeContent( Type type )
    {
        this.Type = type;
    }

    @Override
    public byte[] getByteMessage()
    {
        return new byte[0];
    }

    @Override
    public String getStringMessage()
    {
        return new String();
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
