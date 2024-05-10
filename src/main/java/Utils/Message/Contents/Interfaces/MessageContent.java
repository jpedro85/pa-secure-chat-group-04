package Utils.Message.Contents.Interfaces;

import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

import java.io.Serializable;

/**
 * The MessageContent interface represents the content of a message in a messaging system.
 */
public interface MessageContent extends Serializable {

    /**
     * Returns the byte representation of the message content.
     *
     * @return byte array representing the message content.
     */
    byte[] getByteMessage();

    /**
     * Returns the string representation of the message content.
     *
     * @return string representing the message content.
     */
    String getStringMessage();

    /**
     * Returns the type of the message content.
     *
     * @return ContentTypes representing the type of the message content.
     */
    ContentTypes getType();

    /**
     * Returns the subtype of the message content.
     *
     * @return ContentSubtype representing the subtype of the message content.
     */
    ContentSubtype getSubType();
}
