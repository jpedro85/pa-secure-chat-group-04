package Utils.Message.EnumTypes;

public enum AccountMessageTypes implements ContentSubtype
{
    REGISTER(ContentTypes.ACCOUNT),
    LOGIN(ContentTypes.ACCOUNT),
    LOGGED_USERS(ContentTypes.ACCOUNT),
    LOGOUT(ContentTypes.ACCOUNT);

    private final ContentTypes TYPE;
    AccountMessageTypes(ContentTypes type){
        TYPE = type;
    }

    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
