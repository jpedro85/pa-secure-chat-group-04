package Utils.Message.Contents.Interfaces;

/**
 * The MessageContentIntegrityMAC interface represents message content that includes integrity MAC (Message Authentication Code) information.
 * This interface extends the MessageContent interface.
 */
public interface MessageContentIntegrityMAC extends MessageContent {

    /**
     * Returns the MAC (Message Authentication Code) associated with the message content.
     *
     * @return byte array representing the MAC (Message Authentication Code) of the message content.
     */
    byte[] getMAC();

    /**
     * Checks if the message content has a valid MAC (Message Authentication Code) using the provided secret.
     *
     * @param secret byte array representing the secret key used to generate the MAC.
     * @return true if the message content's MAC is valid, false otherwise.
     */
    boolean hasValidMAC(byte[] secret);
}
