package Utils.Message.Contents;

import java.io.Serializable;

public interface IMessageContent extends Serializable
{
    byte[] getByteMessage ();

    String getStringMessage ();

    ContentType getType();

}
