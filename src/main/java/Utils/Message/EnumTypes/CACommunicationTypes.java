package Utils.Message.EnumTypes;

public enum CACommunicationTypes implements ContentSubtype
{
    SIGNE(ContentTypes.CA_COMMUNICATION),
    REVOKE(ContentTypes.CA_COMMUNICATION),
    CERTIFICATE_STATE(ContentTypes.CA_COMMUNICATION),
    PUBLIC_KEY(ContentTypes.CA_COMMUNICATION);

    private final ContentTypes TYPE;
    CACommunicationTypes(ContentTypes type){
        TYPE = type;
    }

    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
