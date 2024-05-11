package Utils.Message.EnumTypes;

/**
 * This enum contains types for account related messages.
 */
public enum AccountMessageTypes implements ContentSubtype
{
    /**Type of the message, to send register information*/
    REGISTER(ContentTypes.ACCOUNT),

    /** Type of the message for user login. */
    LOGIN(ContentTypes.ACCOUNT),

    /** Type of the message for renewing login credentials. */
    LOGIN_RENOVATE(ContentTypes.ACCOUNT),

    /** Type of the message to retrieve logged-in users. */
    LOGGED_USERS(ContentTypes.ACCOUNT),

    /** Type of the message for user logout. */
    LOGOUT(ContentTypes.ACCOUNT);

    private final ContentTypes TYPE;

    /**
     * Constructs a AccountMessageTypes enum with the specified ContentTypes.
     * @param type the ContentTypes of the AccountMessageTypes type
     */
    AccountMessageTypes(ContentTypes type){
        TYPE = type;
    }

    @Override
    public ContentTypes getSuperType() {
        return TYPE;
    }
}
