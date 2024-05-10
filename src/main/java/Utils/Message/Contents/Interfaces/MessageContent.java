package Utils.Message.Contents.Interfaces;

import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

import java.io.Serializable;

public interface MessageContent extends Serializable
{
    byte[] getByteMessage ();

    String getStringMessage ();

    ContentTypes getType();
    ContentSubtype getSubType();

}
