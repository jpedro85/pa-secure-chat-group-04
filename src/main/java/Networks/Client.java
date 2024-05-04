package Networks;

import Utils.Certificate.CertificateGenerator;
import Utils.Certificate.CustomCertificate;
import Utils.Certificate.PEMCertificateEncoder;
import Utils.Concurrency.VarSync;
import Utils.Config.Config;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;
import Utils.Message.Contents.*;
import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.Contents.Interfaces.MessageContentIntegrityMAC;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.ContentTypes;
import Utils.Message.Message;
import Utils.Security.DiffieHellman;
import Utils.Security.Encryption.RSA;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client
{
    private final Logger LOGGER;
    private final Config CONFIG;
    private final Scanner SCANNER;
    private KeyPair clientKeyPair;

    private PublicKey caPublicKey;

    private BigInteger sharedDHSecretCA;
    private VarSync<ArrayList<User> > connectedUsers;
    private User client;
    private BlockingQueue<Message> queue;

    /**
     * Server Msg Connection
     */
    private final Socket MSG_SERVER_CONNECTION;
    private final ObjectInputStream MSG_SERVER_CONNECTION_INPUT;
    private final ObjectOutputStream MSG_SERVER_CONNECTION_OUTPUT;

    /**
     * Server CA Connection
     */
    private final Socket CA_SERVER_CONNECTION;
    private final ObjectInputStream CA_SERVER_CONNECTION_INPUT;
    private final ObjectOutputStream CA_SERVER_CONNECTION_OUTPUT;


    public Client(Config config, Logger logger)
    {
        LOGGER = logger;
        CONFIG = config;
        queue = new LinkedBlockingQueue<>();
        SCANNER = new Scanner( System.in );

        try
        {
            MSG_SERVER_CONNECTION = connect( CONFIG.getMsgServerPort() );
            MSG_SERVER_CONNECTION_OUTPUT = new ObjectOutputStream( MSG_SERVER_CONNECTION.getOutputStream() );
            MSG_SERVER_CONNECTION_INPUT = new ObjectInputStream( MSG_SERVER_CONNECTION.getInputStream() );

            CA_SERVER_CONNECTION = connect( CONFIG.getCaServerPort() );
            CA_SERVER_CONNECTION_OUTPUT = new ObjectOutputStream( CA_SERVER_CONNECTION.getOutputStream() );
            CA_SERVER_CONNECTION_INPUT = new ObjectInputStream( CA_SERVER_CONNECTION.getInputStream() );
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void start() throws IOException
    {
        client = register();
        CustomCertificate certificate = createCertificate( client.getUsername() );

        // TODO: remove after test
        certificate = askSigneCertificate( certificate );
        System.out.println( certificate.getIssuer() );
        System.out.println( certificate.getSignature() );
        //--}

        client.setCertificate( new PEMCertificateEncoder().encode( certificate ) );
        caPublicKey = askPublicKey();
        if (caPublicKey == null)
            logOut();
        else
        {
            logIn();
            connectedUsers = askConnectedUsers();
        }

    }

    private Socket connect(int port)
    {
        try
        {
            return new Socket( "localhost" , port );
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

//    private void ListUsers()
//    {
//
//    }
//
//    private void ListMessages()
//    {
//
//    }
//
//    private void sendMessage()
//    {
//
//    }

    private User register()
    {
        do
        {
            System.out.println("Set username:");
            String username = SCANNER.nextLine();

            if( username.isBlank()  )
            {
                System.out.println( "Invalid username: " + username );
                continue;
            }

            if ( requestRegister( username ) )
            {
                return new User( username );
            }
        }
        while ( true );
    }

    private boolean requestRegister( String username )
    {
        try
        {
            MessageContent content = ContentFactory.createRegisterContent( username );
            MSG_SERVER_CONNECTION_OUTPUT.writeObject( new Message( username, "Server", content ) );

            Message msg = (Message)MSG_SERVER_CONNECTION_INPUT.readObject();

            switch ( msg.getContent().getType() )
            {
                case ERROR -> {
                    LOGGER.log( msg.getContent().getStringMessage(),  Optional.of(LogTypes.INFO) );
                    return false;
                }

                case ACCOUNT -> { return handleAccountRequestRegisterResponse(msg); }

                default -> { throw new RuntimeException(" Invalid Message received " + msg.getContent().getType() + " " + msg.getContent().getSubType() ); }
            }

        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }

    }

    private boolean handleAccountRequestRegisterResponse( Message message )
    {
        if ( message.getContent().getSubType() == AccountMessageTypes.REGISTER )
            return true;
        else
            throw new RuntimeException(" Invalid Message received " + message.getContent().getType() + " " + message.getContent().getSubType() );
    }

    private CustomCertificate createCertificate( String username)
    {
        clientKeyPair = RSA.generateKeyPair();
        return new CertificateGenerator()
            .forSubject( username )
            .issuedBy("none")
            .withPublicKey( clientKeyPair.getPublic() )
            .generate();

    }

    private PublicKey askPublicKey()
    {
        try
        {
            MessageContent content = ContentFactory.createTypeContent( CACommunicationTypes.PUBLIC_KEY );
            CA_SERVER_CONNECTION_OUTPUT.writeObject( new Message( client.getUsername(), "CA" , content  ));
            Message msg = (Message)CA_SERVER_CONNECTION_INPUT.readObject();


            if (msg.getContent().getType() == ContentTypes.CA_COMMUNICATION && msg.getContent().getSubType() == CACommunicationTypes.PUBLIC_KEY)
            {
                if ( ((PublicKeyContent)msg.getContent()).hasValidMAC( sharedDHSecretCA.toByteArray() ) )
                {
                    PublicKey key = ((PublicKeyContent) msg.getContent()).getPublicKey();
                    LOGGER.log("Ca public key." + key, Optional.of(LogTypes.DEBUG));
                    return key;
                }
                else
                {
                    LOGGER.log( "Received msg has invalid MAC." + msg.getContent().getStringMessage(), Optional.of(LogTypes.ERROR) );
                }
            }

            else if (msg.getContent().getType() == ContentTypes.ERROR )
                LOGGER.log( "Received an error when asking for public key." + msg.getContent().getStringMessage(), Optional.of(LogTypes.ERROR) );

        }
        catch (IOException | ClassNotFoundException e)
        {
            LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
        }
        return null;
    }

    private CustomCertificate askSigneCertificate( CustomCertificate certificate )
    {
        String fileName = client.getUsername()+".pem" ;
        try ( FileWriter fileWriter = new FileWriter( "src/data/Certificates/" + fileName) )
        {
            sharedDHSecretCA = agreeOnSecret(CA_SERVER_CONNECTION_OUTPUT,CA_SERVER_CONNECTION_INPUT,"CA");
            PEMCertificateEncoder encoder = new PEMCertificateEncoder();
            fileWriter.write( encoder.encode( certificate ) );
            fileWriter.close();

            CA_SERVER_CONNECTION_OUTPUT.writeObject( new Message( client.getUsername(), "CA", ContentFactory.createSigneContent( fileName , sharedDHSecretCA ) ) );
            Message msg = (Message)CA_SERVER_CONNECTION_INPUT.readObject();

            if ( ! (msg.getContent().getType() == ContentTypes.CA_COMMUNICATION && msg.getContent().getSubType() == CACommunicationTypes.SIGNE) )
                throw new RuntimeException(  String.format( "Invalid response on login type:%s subtype:%s msg:%s", msg.getContent().getType(), msg.getContent().getSubType() , msg.getContent().getStringMessage() ) ) ;

            IntegrityContent content = (IntegrityContent) msg.getContent();

            if( ! content.hasValidMAC( sharedDHSecretCA.toByteArray() ) )
                throw new RuntimeException( "Compromised Message on login" ) ;

            return encoder.decode( content.getStringMessage() );

        }
        catch ( Exception e )
        {
            throw new RuntimeException( e) ;
        }
    }

    private VarSync<ArrayList<User>> askConnectedUsers() throws IOException
    {
        try
        {
            Message msg = (Message)MSG_SERVER_CONNECTION_INPUT.readObject();

            if ( msg.getContent().getType() == ContentTypes.ACCOUNT && msg.getContent().getSubType() == AccountMessageTypes.LOGGED_USERS)
            {
                if ( ( (AllLoggedInContent)msg.getContent() ).hasValidDigest() )
                    return new VarSync<>( ( (AllLoggedInContent)msg.getContent() ).getUsers() );
                else
                    LOGGER.log( "Invalid Digest." , Optional.of(LogTypes.ERROR ));
            }

            LOGGER.log( "Invalid message type received when waiting for users." , Optional.of(LogTypes.ERROR ));
            throw new RuntimeException("Invalid message");
        }
        catch ( ClassNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }

    private BigInteger agreeOnSecret( ObjectOutputStream outputStream, ObjectInputStream inputStream, String recipient )
    {
        try
        {
            BigInteger privateKey = DiffieHellman.generatePrivateKey();
            BigInteger publicKey = DiffieHellman.generatePublicKey( privateKey );

            outputStream.writeObject( new Message( client.getUsername(), recipient, ContentFactory.createDiffieHellmanContent( publicKey )  ) );
            Message msg =  (Message) inputStream.readObject();

            switch ( msg.getContent().getType() )
            {
                case ERROR -> { throw new RuntimeException( "Invalid Message received on dh key exchange." ); }

                case DIFFIE_HELLMAN -> {

                    DiffieHellmanContent content = (DiffieHellmanContent)msg.getContent();

                    if ( content.hasValidDigest() )
                        return DiffieHellman.computeSecret( content.getPublic_key(), privateKey );
                    else
                    {
                        throw new RuntimeException( "Invalid Message received on dh key exchange." );
                    }
                }

                default -> throw new RuntimeException( "Invalid Message received on dh key exchange." );
            }

        }catch (NoSuchAlgorithmException | IOException | ClassNotFoundException e)
        {
            throw new RuntimeException( e);
        }

    }

    private void logIn() throws IOException
    {
        try
        {
            MessageContent loginContent = ContentFactory.createLoginContent( client.getCertificate() , client.getUsername() );
            MSG_SERVER_CONNECTION_OUTPUT.writeObject( new Message( client.getUsername(), "MSGServer", loginContent  ));

            Message msg = (Message) MSG_SERVER_CONNECTION_INPUT.readObject();

            switch ( msg.getContent().getType() ) {
                case ERROR -> {
                    LOGGER.log(msg.getContent().getStringMessage(), Optional.of(LogTypes.ERROR));
                }

                case ACCOUNT -> {

                    if (msg.getContent().getSubType() != AccountMessageTypes.LOGIN)
                        LOGGER.log("Invalid message received" + msg.getContent().getStringMessage(), Optional.of(LogTypes.ERROR));

                    LOGGER.log(  "You have loged in ." , Optional.of(LogTypes.INFO));

                }

                default -> throw new RuntimeException("Invalid Message received on dh key exchange.");
            }

        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }

    }

    private void logOut()
    {

    }

}
