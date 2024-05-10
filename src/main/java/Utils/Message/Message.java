package Utils.Message;

import Utils.Message.Contents.Interfaces.MessageContent;

import java.io.Serializable;

/**
 * This class models a message with a sender, a recipient, and content.
 * It is designed to be a part of a messaging application where messages are sent and received.
 * <p>
 * The {@code Message} class is immutable, meaning once a message is created, the sender,
 * recipient, and content cannot be changed.
 * </p>
 *
 * @author
 * @version
 */
public class Message implements Serializable
{

    private final String SENDER;

    private final String RECIPIENT;

    private final MessageContent CONTENT;


    /**
     * Constructs a new {@code Message} with the specified sender, recipient, and content.
     *
     * @param sender    the sender's identifier, cannot be {@code null}
     * @param recipient the recipient's identifier, cannot be {@code null}
     * @param content   the content of the message, must implement {@link MessageContent}
     */
    public Message(String sender, String recipient, MessageContent content)
    {
        this.SENDER = sender;
        this.RECIPIENT = recipient;
        this.CONTENT = content;
    }

    /**
     * Returns the sender of the message.
     *
     * @return the sender's identifier
     */
    public String getSender()
    {
        return SENDER;
    }

    /**
     * Returns the recipient of the message.
     *
     * @return the recipient's identifier
     */
    public String getRecipient()
    {
        return RECIPIENT;
    }

    /**
     * Returns the content of the message.
     *
     * @return the content of the message, represented by an instance of {@link MessageContent}
     */
    public MessageContent getContent()
    {
        return CONTENT;
    }

}
