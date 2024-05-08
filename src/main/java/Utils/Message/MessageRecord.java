package Utils.Message;

/**
 * Represents a record of a message, including the sender and the message content.
 */
public class MessageRecord {

    /**
     * The sender of the message.
     */
    private final String SENDER;

    /**
     * The content of the message.
     */
    private final String MESSAGE;

    /**
     * Constructs a new MessageRecord with the specified sender and message content.
     * @param SENDER the sender of the message
     * @param MESSAGE the content of the message
     */
    public MessageRecord(String SENDER, String MESSAGE) {
        this.SENDER = SENDER;
        this.MESSAGE = MESSAGE;
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
}
