package Utils.Message.EnumTypes;

/**
 * Represents the types of message needed for DiffieHellman secret exchange.
 */
public enum DiffieHellmanTypes implements ContentSubtype
{
    /**
     * Represents a message for exchanging publicDH messages.
     */
    KEY_CHANGE(ContentTypes.DIFFIE_HELLMAN);

    private final ContentTypes TYPE;

    /**
     * Constructs a DiffieHellmanTypes enum with the specified ContentTypes.
     * @param type the ContentTypes of the DiffieHellman type.
     */
    DiffieHellmanTypes(ContentTypes type){
        TYPE = type;
    }

    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
