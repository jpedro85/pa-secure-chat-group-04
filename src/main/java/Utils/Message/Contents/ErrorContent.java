package Utils.Message.Contents;

import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.EnumTypes.ContentSubtype;

public class ErrorContent implements MessageContent
{

    private final MessageContent CONTENT;
    private final String ERROR;

    public ErrorContent(MessageContent content , String error) {
        this.CONTENT = content;
        this.ERROR = error;
    }

    public MessageContent getContent()
    {
        return CONTENT;
    }

    @Override
    public byte[] getByteMessage() {
        return ERROR.getBytes();
    }

    @Override
    public String getStringMessage() {
        return ERROR;
    }

    @Override
    public ContentTypes getType() {
        return ContentTypes.ERROR;
    }

    @Override
    public ContentSubtype getSubType() {
        return CONTENT.getSubType() ;
    }
}
