package Utils.Message;

import java.util.Date;

/**
 * Represents a record of a message, capturing details about the sender, the content of the message,
 * and the date when the message was received. This class is used to track and display message history
 * in a messaging system.
 * <p>
 * The class is immutable, ensuring that once a {@code MessageRecord} is created, its fields cannot be changed.
 * </p>
 */
public class MessageRecord
{
    private final String SENDER;

    private final String MESSAGE;

    private final String RECEIVE_DATE;

    /**
     * Constructs a new {@code MessageRecord} with the specified sender, message content, and the
     * received date.
     *
     * @param SENDER the sender of the message
     * @param MESSAGE the content of the message
     * @param RECEIVE_DATE the date and time when the message was received, as a string
     */
    public MessageRecord(String SENDER, String MESSAGE, String RECEIVE_DATE) {
        this.SENDER = SENDER;
        this.MESSAGE = MESSAGE;
        this.RECEIVE_DATE = RECEIVE_DATE;
    }

    /**
     * Returns the sender of the message.
     *
     * @return the sender's identifier
     */
    public String getSender() {
        return SENDER;
    }

    /**
     * Returns the content of the message.
     *
     * @return the textual content of the message
     */
    public String getMessage() {
        return MESSAGE;
    }

    /**
    * Returns the date and time when the message was received.
    *
    * @return the received date and time as a string
    */
    public String getReceivedDate() {
        return RECEIVE_DATE;
    }

    /**
     * Provides a string representation of this message record, formatted to show the received date,
     * sender, and the message content in a readable form.
     *
     * @return a string representation of the message record
     */
    @Override
    public String toString()
    {
        return "At:" + RECEIVE_DATE  + " Message from: " + SENDER + '\n' + "Message: " + MESSAGE ;
    }
}
