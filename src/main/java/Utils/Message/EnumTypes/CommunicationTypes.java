package Utils.Message.EnumTypes;

/**
 * Enumeration representing different types of communication content.
 */
public enum CommunicationTypes implements ContentSubtype {
    /**
     * Represents a complete encrypted message.
     */
    MSG(ContentTypes.COMMUNICATION),

    /**
     * Represents messages, that notify other user of an invalid certificate.
     */
    INVALID_CERTIFICATE(ContentTypes.COMMUNICATION);

    private final ContentTypes TYPE;

    /**
     * Constructs a CommunicationTypes enum with the specified ContentTypes.
     * @param type the ContentTypes of the communication type
     */
    CommunicationTypes(ContentTypes type) {
        TYPE = type;
    }

    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
