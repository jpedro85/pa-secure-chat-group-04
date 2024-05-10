package Utils.Message;

import java.util.Date;

/**
 * Represents a record of a message, including the sender and the message content.
 */
public class MessageRecord
{
    /**
     * The sender of the message.
     */
    private final String SENDER;

    /**
     * The content of the message.
     */
    private final String MESSAGE;

    private final String RECEIVE_DATE;

    /**
     * Constructs a new MessageRecord with the specified sender and message content.
     * @param SENDER the sender of the message
     * @param MESSAGE the content of the message
     */
    public MessageRecord(String SENDER, String MESSAGE, String RECEIVE_DATE) {
        this.SENDER = SENDER;
        this.MESSAGE = MESSAGE;
        this.RECEIVE_DATE = RECEIVE_DATE;
    }

    /**
     * Gets the sender of the message.
     * @return the sender of the message
     */
    public String getSender() {
        return SENDER;
    }

    /**
     * Gets the content of the message.
     * @return the content of the message
     */
    public String getMessage() {
        return MESSAGE;
    }

    public String getReceivedDate() {
        return RECEIVE_DATE;
    }

    @Override
    public String toString()
    {
        return "At:" + RECEIVE_DATE  + " Message from: " + SENDER + '\n' + "Message: " + MESSAGE ;
    }
}
