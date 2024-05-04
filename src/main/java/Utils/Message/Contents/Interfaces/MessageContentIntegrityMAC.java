package Utils.Message.Contents.Interfaces;

public interface MessageContentIntegrityMAC extends MessageContent
{
    byte[] getMAC();
    boolean hasValidMAC( byte[] secret );
}
