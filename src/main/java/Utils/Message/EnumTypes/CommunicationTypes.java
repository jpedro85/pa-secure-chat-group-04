package Utils.Message.EnumTypes;

/**
 * Enumeration representing different types of communication content.
 */
public enum CommunicationTypes implements ContentSubtype {
    /**
     * Represents a complete encrypted message.
     */
    MSG(ContentTypes.COMMUNICATION),
    INVALID_CERTIFICATE(ContentTypes.COMMUNICATION);

    private final ContentTypes TYPE;

    /**
     * Constructs a CommunicationTypes enum with the specified ContentTypes.
     * @param type the ContentTypes of the communication type
     */
    CommunicationTypes(ContentTypes type) {
        TYPE = type;
    }

    /**
     * Gets the super type of this communication type.
     * @return the ContentTypes super type
     */
    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
