package Utils.Message.Contents.Interfaces;

public interface MessageContentIntegrityHash extends MessageContent
{
    byte[] getDigest();
    boolean hasValidDigest();
}
