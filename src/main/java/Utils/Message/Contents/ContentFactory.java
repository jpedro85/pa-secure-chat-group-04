package Utils.Message.Contents;

import Networks.User;
import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.CommunicationTypes;
import Utils.Message.EnumTypes.ContentSubtype;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.ArrayList;

public class ContentFactory
{
    private ContentFactory(){}

    public static MessageContent createTypeContent(ContentSubtype type) {
        if (type == null) {
            throw new IllegalArgumentException("Invalid argument: type cannot be null");
        }
        return new TypeContent(type);
    }

    public static MessageContent createIntegrityContent(String content, BigInteger secret, ContentSubtype type) {
        if (content == null || secret == null || type == null) {
            throw new IllegalArgumentException("Invalid arguments: content, secret, and type cannot be null");
        }
        return new IntegrityContent(content, secret, type);
    }

    public static MessageContent createSigneContent(String Filename, BigInteger secret) {
        if (Filename == null || secret == null) {
            throw new IllegalArgumentException("Invalid arguments: Filename and secret cannot be null");
        }
        return new IntegrityContent(Filename, secret, CACommunicationTypes.SIGNE);
    }

    public static MessageContent createPublicKeyContent(PublicKey publicKey, BigInteger secret) {
        if (publicKey == null || secret == null) {
            throw new IllegalArgumentException("Invalid arguments: public key and secret cannot be null");
        }
        return new PublicKeyContent(publicKey, secret);
    }

    public static MessageContent createErrorContent(MessageContent content, String error) {
        if (content == null || error == null) {
            throw new IllegalArgumentException("Invalid arguments: content and error cannot be null");
        }
        return new ErrorContent(content, error);
    }

    public static MessageContent createRegisterContent(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Invalid argument: username cannot be null");
        }
        return new RegisterContent(username);
    }

    public static MessageContent createLoginContent(String certificate, String username) {
        if (certificate == null || username == null) {
            throw new IllegalArgumentException("Invalid arguments: certificate and username cannot be null");
        }
        return new LogInContent(certificate, username);
    }

    public static MessageContent createLoginRenovateContent(String certificate, String username) {
        if (certificate == null || username == null) {
            throw new IllegalArgumentException("Invalid arguments: certificate and username cannot be null");
        }
        return new LogInRenovateContent(certificate, username);
    }

    public static MessageContent createCertificateStateContent( int serialNumber, boolean isValid ) {
        return new CertificateState( isValid, serialNumber);
    }

    public static MessageContent createCertificateStateContent( int serialNumber )
    {
        return new CertificateState(false,serialNumber);
    }

    public static MessageContent createCertificateStateInvalidContent( int serialNumber )
    {
        return new CertificateStateInvalid( serialNumber );
    }

    public static MessageContent createLogoutContent(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Invalid argument: username cannot be null");
        }
        return new LogOutContent(username);
    }

    public static MessageContent createDiffieHellmanContent(BigInteger publicKey) {
        if (publicKey == null) {
            throw new IllegalArgumentException("Invalid argument: public key cannot be null");
        }
        return new DiffieHellmanKeyChangeContent( publicKey.toByteArray() );
    }

    public static MessageContent createDiffieHellmanRSAContent( byte[] publicKey) {
        if (publicKey == null) {
            throw new IllegalArgumentException("Invalid argument: public key cannot be null");
        }
        return new DiffieHellmanKeyChangeContent(publicKey);
    }

    public static MessageContent createAllLoggedInContent(ArrayList<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("Invalid argument: users cannot be null");
        }
        return new AllLoggedInContent(users);
    }

    public static MessageContent createMSGCommunicationContent( byte[] message )
    {
        if ( message == null) {
            throw new IllegalArgumentException("Invalid argument: message cannot be null");
        }
        return new MessageCommunicationContent(CommunicationTypes.MSG,message);
    }

}
