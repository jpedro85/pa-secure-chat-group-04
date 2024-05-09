package Utils.Message.Contents;

import Utils.Message.EnumTypes.AccountMessageTypes;

public class LogInRenovateContent extends LogInContent
{
    protected LogInRenovateContent(String certificate, String username)
    {
        super(certificate, username, AccountMessageTypes.LOGIN_RENOVATE);
    }
}
