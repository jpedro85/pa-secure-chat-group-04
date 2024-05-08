package Utils.Message.EnumTypes;

public enum DiffieHellmanTypes implements ContentSubtype
{
    KEY_CHANGE(ContentTypes.DIFFIE_HELLMAN);

    private final ContentTypes TYPE;
    DiffieHellmanTypes(ContentTypes type){
        TYPE = type;
    }

    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
