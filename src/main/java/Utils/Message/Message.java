package Utils.Message;

import Utils.Message.Contents.IMessageContent;

import java.io.Serializable;

public class Message implements Serializable
{

    private final String SENDER;

    private final String RECIPIENT;

    private IMessageContent content;

    public Message(String sender, String recipient, IMessageContent content)
    {
        this.SENDER = sender;
        this.RECIPIENT = recipient;
    }

    public String getSender()
    {
        return SENDER;
    }

    public String getRecipient()
    {
        return RECIPIENT;
    }

}
