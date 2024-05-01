package Utils.Message;

import Utils.Message.Contents.MessageContent;

import java.io.Serializable;

public class Message implements Serializable
{

    private final String SENDER;

    private final String RECIPIENT;

    private final MessageContent CONTENT;

    public Message(String sender, String recipient, MessageContent content)
    {
        this.SENDER = sender;
        this.RECIPIENT = recipient;
        this.CONTENT = content;
    }

    public String getSender()
    {
        return SENDER;
    }

    public String getRecipient()
    {
        return RECIPIENT;
    }

    public MessageContent getContent()
    {
        return CONTENT;
    }

}
