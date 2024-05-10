package Utils.Message.Contents.Interfaces;

/**
 * The MessageContentIntegrityHash interface represents message content that includes integrity hash information.
 * This interface extends the MessageContent interface.
 */
public interface MessageContentIntegrityHash extends MessageContent {

    /**
     * Returns the digest (hash) associated with the message content.
     *
     * @return byte array representing the digest (hash) of the message content.
     */
    byte[] getDigest();

    /**
     * Checks if the message content has a valid digest (hash).
     *
     * @return true if the message content's digest (hash) is valid, false otherwise.
     */
    boolean hasValidDigest();
}
