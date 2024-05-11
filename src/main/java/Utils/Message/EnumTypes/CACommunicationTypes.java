package Utils.Message.EnumTypes;


/**
 * This enum contains types for certificate authority (CA) related communication messages.
 */
public enum CACommunicationTypes implements ContentSubtype
{
    /** Type of the message for signing certificates. */
    SIGNE(ContentTypes.CA_COMMUNICATION),
    /** Type of the message for revoking certificates. */
    REVOKE(ContentTypes.CA_COMMUNICATION),
    /** Type of the message to query a certificate state. */
    CERTIFICATE_STATE(ContentTypes.CA_COMMUNICATION),
    /** Type of the message to share public keys. */
    PUBLIC_KEY(ContentTypes.CA_COMMUNICATION);

    private final ContentTypes TYPE;

    /**
     * Constructs a CACommunicationTypes enum with the specified ContentTypes.
     * @param type the ContentTypes of the CACommunication type
     */
    CACommunicationTypes(ContentTypes type){
        TYPE = type;
    }

    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
