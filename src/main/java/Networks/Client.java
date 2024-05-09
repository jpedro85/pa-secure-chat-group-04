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
import Utils.Message.Contents.Interfaces.MessageContentIntegrityHash;
import Utils.Message.EnumTypes.*;
import Utils.Message.Message;
import Utils.Message.MessageRecord;
import Utils.Security.DiffieHellman;
import Utils.Security.Encryption.AES;
import Utils.Security.Encryption.RSA;
import Utils.Security.Integrity.HASH;
import Utils.UserInputs.Command;
import Utils.UserInputs.UserInput;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static Utils.Message.EnumTypes.AccountMessageTypes.LOGOUT;

public class Client
{
    private final Logger LOGGER;
    private final Config CONFIG;
    private final Scanner SCANNER;
    private KeyPair clientKeyPair;

    private PublicKey caPublicKey;

    private BigInteger sharedDHSecretCA;
    private ConcurrentHashMap< String, ClientUser > connectedUsers;
    private ClientUser client;
    private BlockingQueue<MessageRecord> messages;
    private final ReentrantLock AGREE_ON_SECRETE_LOCK;
    private final Condition AGREE_ON_SECRETE_LOCK_CONDITION;
    private VarSync<Boolean> exit;
    private final UserInput USER_INPUT;

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

    private VarSync<Boolean> isLogged;
    private VarSync<Boolean> isWaitingForCertificate;


    public Client(Config config, Logger logger) throws IOException
    {
        LOGGER = logger;
        CONFIG = config;
        messages = new LinkedBlockingQueue<>();
        SCANNER = new Scanner( System.in );
        AGREE_ON_SECRETE_LOCK = new ReentrantLock();
        AGREE_ON_SECRETE_LOCK_CONDITION = AGREE_ON_SECRETE_LOCK.newCondition();
        exit = new VarSync<>(false);
        USER_INPUT = createUserInput();
        isLogged = new VarSync<>(false);
        isWaitingForCertificate = new VarSync<>(false);

        try
        {
            MSG_SERVER_CONNECTION = connect( CONFIG.getMsgServerPort() );
            MSG_SERVER_CONNECTION_OUTPUT = new ObjectOutputStream( MSG_SERVER_CONNECTION.getOutputStream() );
            MSG_SERVER_CONNECTION_INPUT = new ObjectInputStream( MSG_SERVER_CONNECTION.getInputStream() );
        }
        catch (RuntimeException e) { throw new ConnectException("Could not connect to MSGServer");}

        try
        {
            CA_SERVER_CONNECTION = connect( CONFIG.getCaServerPort() );
            CA_SERVER_CONNECTION_OUTPUT = new ObjectOutputStream( CA_SERVER_CONNECTION.getOutputStream() );
            CA_SERVER_CONNECTION_INPUT = new ObjectInputStream( CA_SERVER_CONNECTION.getInputStream() );
        }
        catch (RuntimeException e) { throw new ConnectException("Could not connect to CAServer");}

    }

    private UserInput createUserInput()
    {
        UserInput userInput = new UserInput();
        userInput.addCommand( new Command( "exit", args -> { logOut(); } , "Exit the program. (no arguments)" ) );
        userInput.addCommand( new Command( "listUsers", args -> { ListUsers(); } , "List the connected and authenticated users. (no arguments)" ) );
        userInput.addCommand( new Command( "listMsg", this::ListMessages , "List all received messages. List <username1> <username2> ... " ) );
        userInput.addCommand( new Command( "msg", this::sendCommunicationCommandHandler, "Send a message. msg (Optional)< @<username> @<username> ... > <Message>" ) );
        userInput.addCommand( new Command( "revoke", this::revokeCertificate  , "Revoke the certificate of the user.To this is user if user is omitted. revoke (Optional)<user>"  ) );

        return userInput;
    }


    private ClientListener listener ;

    public void start() throws IOException
    {
        client = register();
        CustomCertificate certificate = createCertificate( client.getUsername() );

//        certificate = askSigneCertificate( certificate );

        client.setCertificate( askSigneCertificate( certificate ) );
        caPublicKey = askPublicKey();

        if (caPublicKey == null)
            logOut();
        else
        {
            logIn();
            connectedUsers = askConnectedUsers();
            validateConnectedUsers();
            listener = new ClientListener();
            listener.start();

            while ( !exit.syncGet() )
                USER_INPUT.executeFromInput();


            CloseConnections();
            listener.close();
        }

    }

    private void CloseConnections() throws IOException
    {
        MSG_SERVER_CONNECTION.close();
        CA_SERVER_CONNECTION.close();
    }

    private void validateConnectedUsers()
    {
        Iterator<ClientUser> userIterator = connectedUsers.values().iterator();
        ClientUser user;
        PEMCertificateEncoder encoder = new PEMCertificateEncoder();
        while( userIterator.hasNext() )
        {
            user = userIterator.next();
            if( ! isValidUserCertificate( user ) )
            {
                try
                {
                    connectedUsers.remove( user.getUsername() );
                    CustomCertificate certificate = encoder.decode( user.getCertificate() ) ;
                    sendInvalidCertificateMessage( user, certificate );
                }
                catch (ClassNotFoundException | IOException e)
                {
                    LOGGER.log("Could decode certificate.",Optional.of(LogTypes.ERROR));
                }
            }
            else
                LOGGER.log( "The user '" + user.getUsername() + "' has been connected.", Optional.of(LogTypes.INFO));

        }


    }

    private boolean isValidUserCertificate( User user )
    {
        try
        {
            return isValidUserCertificate(user, new PEMCertificateEncoder().decode( user.getCertificate() ) );
        }
        catch (IOException | ClassNotFoundException e)
        {
            LOGGER.log( "Couldn't decode user certificate '" + user.getUsername() + "'" , Optional.of(LogTypes.ERROR));
        }
        return false;
    }
    private boolean isValidUserCertificate( User user , CustomCertificate certificate )
    {
        try
        {
            byte[] digest = HASH.generateDigest( certificate.getCertificateData() );

            if ( ! Arrays.equals( RSA.decryptRSA( certificate.getSignature() , caPublicKey ) , digest ) )
            {
                LOGGER.log(String.format("User <%s> certificate has an invalid signature.", user.getUsername()), Optional.of(LogTypes.WARN));
                MessageContent content =  ContentFactory.createIntegrityContent( String.valueOf( certificate.getSerialNumber() ), sharedDHSecretCA, CACommunicationTypes.REVOKE );
                sendMessage( new Message( client.getUsername(), "CA", content ), CA_SERVER_CONNECTION_OUTPUT );
                return false;
            }

            if(certificate.getValidTo().getTime() < System.currentTimeMillis())
            {
                LOGGER.log( String.format("User <%s> certificate has expired.", user.getUsername() ), Optional.of(LogTypes.WARN) );
                MessageContent content =  ContentFactory.createIntegrityContent( String.valueOf( certificate.getSerialNumber() ), sharedDHSecretCA, CACommunicationTypes.REVOKE );
                sendMessage( new Message( client.getUsername(), "CA", content ), CA_SERVER_CONNECTION_OUTPUT );
                return false;
            }

            if( checkStateWithCA( certificate.getSerialNumber() ))
            {
                return true;
            }

            return false;
        }
        catch (IOException | ClassNotFoundException e)
        {
            LOGGER.log( "Couldn't decode user certificate '" + user.getUsername() + "'" , Optional.of(LogTypes.ERROR));
            return false;
        }
    }

    private boolean checkStateWithCA( int serialNUmber ) throws IOException, ClassNotFoundException
    {
        MessageContent isRevokeContent = ContentFactory.createCertificateStateContent( serialNUmber );
        CA_SERVER_CONNECTION_OUTPUT.writeObject( new Message( client.getUsername(), "CA", isRevokeContent) );
        Message msg = (Message) CA_SERVER_CONNECTION_INPUT.readObject();

        switch ( msg.getContent().getType() )
        {
            case ERROR -> { LOGGER.log(msg.getContent().getStringMessage(),Optional.of(LogTypes.ERROR)); }

            case CA_COMMUNICATION -> {

                if( ((MessageContentIntegrityHash)msg.getContent()).hasValidDigest() )
                {
                    if (msg.getContent().getSubType() == CACommunicationTypes.CERTIFICATE_STATE) {
                        return ((CertificateState) msg.getContent()).isValid();
                    }
                }
                else
                {
                    LOGGER.log( "received message has invalid digest.",Optional.of(LogTypes.ERROR));
                    return false;
                }
            }

            default -> throw new RuntimeException("Invalid message received ");
        }

        return false;
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

    private void ListUsers()
    {
        if ( connectedUsers.isEmpty() )
        {
            LOGGER.log("There is no connected users.", Optional.of(LogTypes.INFO));
            return;
        }

        for( Map.Entry<String,ClientUser> entry : connectedUsers.entrySet() )
        {
            System.out.println( "User '"+ entry.getKey() + "' is connected." );
        }
    }

    private void ListMessages( String args )
    {
        String[] userNames = args.split(" ");

        if( args.isBlank() )
        {
            for ( MessageRecord msg : messages )
            {
                System.out.println( msg );
            }
        }
        else
        {
            for (int i = 0; i < userNames.length ; i++) {
                ListMessagesOfUser( userNames[i] );
            }
        }
    }
    private void ListMessagesOfUser( String userName )
    {
        if( userName.isBlank() )
            return;

        System.out.println( "Showing Messages of: " + userName );

        int messagesCount = 0;
        for ( MessageRecord msg : messages ) {

            if( msg.getSender().equals( userName ) )
            {
                System.out.println( msg );
                messagesCount++;
            }
        }

        if( messagesCount == 0)
            System.out.println("No messages where found.");
    }

    private void sendCommunicationCommandHandler(String args )
    {
        List<String> usersToSend = new ArrayList<>();

        int index;
        int spaceIndex = 0;
        int nextSpace = 0;
        while ( (index = args.indexOf("@",spaceIndex)) != -1 )
        {
            nextSpace = args.indexOf(" ",index) ;
            if (nextSpace == -1)
                break;
            else
                spaceIndex = nextSpace;
            usersToSend.add( args.substring( index + 1, spaceIndex ) );
        }

        if (nextSpace == -1)
        {
            LOGGER.log( "no message",Optional.of(LogTypes.ERROR));
            return;
        }

        Iterator<String> usersNames;
        if(usersToSend.isEmpty())
            usersNames = connectedUsers.keys().asIterator();
        else
            usersNames = usersToSend.iterator();

        String msg = args.substring(spaceIndex+1 );
        sendMessage( usersNames, msg);
    }
    private void sendMessage( Iterator<String> usersToSend, String message )
    {
        ClientUser userToSend;
        String user;
        while ( usersToSend.hasNext() )
        {
            user = usersToSend.next();
            userToSend = connectedUsers.get( user );
            if( userToSend == null )
            {
                LOGGER.log("Invalid username:" + user, Optional.of(LogTypes.ERROR) );
            }
            else
            {
                if( !userToSend.hasAgreedOnSecret() )
                    startAgreeingOnSecret( userToSend );

                LOGGER.log("Sending msg "+ message + " to:" + user, Optional.of(LogTypes.DEBUG) );
                sendCommunication(userToSend,message);
            }
        }
    }

    private void startAgreeingOnSecret( ClientUser userToSendAgreeSecret )
    {
        userToSendAgreeSecret.setAgreeingOnSecret(true);
        userToSendAgreeSecret.setGeneratedPrivateKey( DiffieHellman.generatePrivateKey() );
        userToSendAgreeSecret.setGeneratedPublicKey( DiffieHellman.generatePublicKey( userToSendAgreeSecret.getGeneratedPrivateKey() ));
        byte[] encryptedDHPublicKey = RSA.encryptRSA( userToSendAgreeSecret.getGeneratedPublicKey().toByteArray() , clientKeyPair.getPrivate() );
        sendMessage(
                new Message( client.getUsername(), userToSendAgreeSecret.getUsername(), ContentFactory.createDiffieHellmanRSAContent( encryptedDHPublicKey ) ),
                MSG_SERVER_CONNECTION_OUTPUT
        );

        AGREE_ON_SECRETE_LOCK.lock();
        try {
            while ( !userToSendAgreeSecret.hasAgreedOnSecret() ) {
                AGREE_ON_SECRETE_LOCK_CONDITION.await(1, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.log(e.getMessage(), Optional.of(LogTypes.ERROR));
        } finally {
            AGREE_ON_SECRETE_LOCK.unlock(); // Always release the lock
        }
    }

    private void sendCommunication( ClientUser recipient, String msg )
    {
        try
        {
            byte[] encryptMessage = AES.encryptMessage(msg, recipient.getSharedSecret().toByteArray() );

            sendMessage(
                    new Message( client.getUsername(), recipient.getUsername(), ContentFactory.createMSGCommunicationContent(encryptMessage) ),
                    MSG_SERVER_CONNECTION_OUTPUT
                    );

        }
        catch (Exception e)
        {
            LOGGER.log( "Could not encrypt message :" + e.getMessage(), Optional.of(LogTypes.ERROR) );
        }
    }

    public void sendMessage( Message message, ObjectOutputStream output )
    {
        try
        {
            output.writeObject( message );
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void sendInvalidCertificateMessage( ClientUser user, CustomCertificate certificate )
    {
        sendMessage(
                new Message(
                        client.getUsername(),
                        "", // send broadcast
                        ContentFactory.createCertificateStateInvalidContent( certificate.getSerialNumber() )
                ),
                MSG_SERVER_CONNECTION_OUTPUT
        );
    }

    private ClientUser register()
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
                return new ClientUser( username );
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
        CustomCertificate certificate = new CertificateGenerator()
            .forSubject( username )
            .issuedBy("none")
            .withPublicKey( clientKeyPair.getPublic() )
            .generate();

        LOGGER.log("Created certificate number " + certificate.getSerialNumber(),Optional.of(LogTypes.INFO ));
        return certificate;
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
                    LOGGER.log("Received CA public key.", Optional.of(LogTypes.DEBUG));
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

    private String askSigneCertificate( CustomCertificate certificate )
    {
        isWaitingForCertificate.syncSet(true);
        String fileName = client.getUsername()+".pem" ;
        try ( FileWriter fileWriter = new FileWriter( "src/data/Certificates/" + fileName) )
        {
            sharedDHSecretCA = agreeOnSecretWithCA(CA_SERVER_CONNECTION_OUTPUT,CA_SERVER_CONNECTION_INPUT,"CA");
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

            LOGGER.log("Received Signed Certificate.", Optional.of(LogTypes.DEBUG ));
            return content.getStringMessage();

        }
        catch ( Exception e )
        {
            throw new RuntimeException( e) ;
        }
        finally {
            isWaitingForCertificate.syncSet(false);
        }
    }

    private ConcurrentHashMap<String,ClientUser> askConnectedUsers() throws IOException
    {
        try
        {
            Message msg = (Message)MSG_SERVER_CONNECTION_INPUT.readObject();

            if ( msg.getContent().getType() == ContentTypes.ACCOUNT && msg.getContent().getSubType() == AccountMessageTypes.LOGGED_USERS)
            {
                if ( ( (AllLoggedInContent)msg.getContent() ).hasValidDigest() )
                {

                    ArrayList<User> array =  ((AllLoggedInContent)msg.getContent()).getUsers();
                    ConcurrentHashMap<String,ClientUser> clientUserList = new ConcurrentHashMap<>();

                    for (User user : array) {

                        ClientUser newUser = new ClientUser( user.getUsername() );
                        newUser.setCertificate( user.getCertificate() );
                        clientUserList.put( newUser.getUsername(), newUser );
                    }

                    return clientUserList;
                }
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

    private BigInteger agreeOnSecretWithCA(ObjectOutputStream outputStream, ObjectInputStream inputStream, String recipient )
    {
        try
        {
            BigInteger privateKey = DiffieHellman.generatePrivateKey();
            BigInteger publicKey = DiffieHellman.generatePublicKey( privateKey );

            outputStream.writeObject( new Message( client.getUsername(), recipient, ContentFactory.createDiffieHellmanContent( publicKey )  ) );
            Message msg =  (Message) inputStream.readObject();

            switch ( msg.getContent().getType() )
            {

                case DIFFIE_HELLMAN -> {

                    DiffieHellmanKeyChangeContent content = (DiffieHellmanKeyChangeContent)msg.getContent();

                    if ( content.hasValidDigest() )
                        return DiffieHellman.computeSecret( content.getPublic_key(), privateKey );
                    else
                    {
                        throw new RuntimeException( "Invalid Message received on dh key exchange." );
                    }
                }

                default -> throw new RuntimeException( "Invalid Message received on dh key exchange. Message:" + msg.getContent().getStringMessage() );
            }

        }catch ( IOException | ClassNotFoundException e)
        {
            throw new RuntimeException( e);
        }

    }


    private void revokeCertificate( String args )
    {
        ClientUser user = connectedUsers.get(args.substring(1));

        if(user != null)
            revokeCertificateOfUser( user );
        else
            revokeThisUserCertificate();
    }

    private void revokeThisUserCertificate()
    {
        try
        {
            CustomCertificate clientCertificate = new PEMCertificateEncoder().decode( client.getCertificate() );
            sendInvalidCertificateMessage(client, clientCertificate );

            CustomCertificate certificate = createCertificate( client.getUsername() );
            client.setCertificate( askSigneCertificate( certificate ) );

            sendMessage( new Message( client.getUsername(), "Server",
                            ContentFactory.createLoginRenovateContent( client.getCertificate(), client.getUsername() )
                    ), MSG_SERVER_CONNECTION_OUTPUT
            );

        }
        catch (IOException |ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void revokeCertificateOfUser( ClientUser user )
    {
        try
        {
            connectedUsers.remove( user.getUsername() );
            LOGGER.log( String.format("Revoked certificate of user '%s'",user.getUsername()),Optional.of(LogTypes.INFO));

            CustomCertificate certificate = new PEMCertificateEncoder().decode( user.getCertificate() );
            String serialNumber = String.valueOf(certificate.getSerialNumber());

            MessageContent content =  ContentFactory.createIntegrityContent( serialNumber, sharedDHSecretCA, CACommunicationTypes.REVOKE );
            sendMessage( new Message( client.getUsername(), "CA", content ), CA_SERVER_CONNECTION_OUTPUT );

            sendInvalidCertificateMessage( user, certificate );
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
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
                    {
                        LOGGER.log("Invalid message received" + msg.getContent().getStringMessage(), Optional.of(LogTypes.ERROR));
                        return;
                    }

                    LOGGER.log(  "You have logged in." , Optional.of(LogTypes.INFO));
                    isLogged.syncSet(true);

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
        try
        {
            LOGGER.log("Logging out...", Optional.of(LogTypes.WARN));
            exit.syncSet(true);
            MSG_SERVER_CONNECTION_OUTPUT.writeObject( new Message(client.getUsername(), "server", ContentFactory.createTypeContent( LOGOUT )));
            isLogged.syncSet(false);
        }
        catch (IOException e)
        {
            LOGGER.log("Couldn't send logout request", Optional.of(LogTypes.ERROR));
        }
    }

    public void terminate() throws InterruptedException
    {
        System.out.println("Terminating");
        if( isLogged.syncGet() )
        {
            this.logOut();

            if(listener != null)
            {
                listener.close();
                listener.join();
            }
        }
        else
            exit.syncSet(true);
    }








    private class ClientListener extends Thread
    {
        private VarSync<Boolean> isRunning;

        public ClientListener()
        {
            this.isRunning = new VarSync<>(false);
        }

        @Override
        public void start()
        {
            this.isRunning.syncSet(true);
            super.start();
        }

        public void close()
        {
            this.isRunning.syncSet(false);
        }
        @Override
        public void run()
        {
            while (this.isRunning.syncGet())
            {
                try
                {
                    handleMessage( (Message)MSG_SERVER_CONNECTION_INPUT.readObject() );
                }
                catch (IOException | ClassNotFoundException e)
                {
                    if( this.isRunning.syncGet() )
                        LOGGER.log("Couldn't read object. " + e.getMessage(), Optional.of(LogTypes.ERROR));
                }
            }
        }

        private void handleMessage( Message message )
        {
            switch ( message.getContent().getType() )
            {
                case ACCOUNT -> { handleAccountMessages( message ); }

                case COMMUNICATION -> { handleCommunicationMessage( message ); }

                case DIFFIE_HELLMAN -> { handleDiffieHellmanMessages( message ); }

                case ERROR -> { LOGGER.log( message.getContent().getStringMessage(), Optional.of(LogTypes.ERROR) ); }

                default -> { throw new RuntimeException("Invalid Message type." + message.getContent().getType() + " " + message.getContent().getStringMessage() );}
            }
        }

        private void handleAccountMessages( Message message )
        {
            switch ( (AccountMessageTypes)message.getContent().getSubType() )
            {
                case LOGIN -> { userLogin( (LogInContent)message.getContent() ); }

                case LOGIN_RENOVATE -> { userLogin( (LogInRenovateContent)message.getContent() ) ;}

                case LOGOUT -> { userLogout( (LogOutContent) message.getContent() ); }

                default -> { throw new RuntimeException("Invalid Message type.");}
            }
        }

        private void handleCommunicationMessage( Message message )
        {
            switch ( (CommunicationTypes)message.getContent().getSubType() )
            {
                case MSG -> { handleMSGMessage( message ); }

                case INVALID_CERTIFICATE -> { handleInvalidCertificateMessage(message); }

                default -> { throw new RuntimeException("Invalid Message type.");}
            }
        }

        private void handleMSGMessage( Message message )
        {
            ClientUser fromUser = connectedUsers.get( message.getSender() );
            if (fromUser == null)
            {
                LOGGER.log( "Invalid Sender (not connected)." + message.getSender(), Optional.of(LogTypes.ERROR) );
                return;
            }

            if( ! ((MessageCommunicationContent)message.getContent()).hasValidDigest() )
            {
                LOGGER.log( "Invalid Sender digest." + message.getSender(), Optional.of(LogTypes.ERROR) );
                return;
            }

            try
            {
                String msgDecrypted = new String( AES.decryptMessage( message.getContent().getByteMessage(), fromUser.getSharedSecret().toByteArray() ) );
                LOGGER.log(String.format("Message from: %s -> %s", message.getSender() ,msgDecrypted ), Optional.of(LogTypes.INFO));
                messages.add(  new MessageRecord( fromUser.getUsername() , msgDecrypted , new SimpleDateFormat("HH:mm:ss").format(new Date()) ) );
            }
            catch (Exception e)
            {
                LOGGER.log( "Could not decrypt message" + e.getMessage(), Optional.of(LogTypes.ERROR) );
            }
        }

        private void handleInvalidCertificateMessage( Message message )
        {

            CertificateState content = (CertificateState)message.getContent();

            if ( !content.hasValidDigest() )
            {
                LOGGER.log( "Received InvalidCertificate message has invalid digest.", Optional.of(LogTypes.ERROR) );
                return;
            }

            try
            {
                PEMCertificateEncoder encoder = new PEMCertificateEncoder();
                CustomCertificate clientCertificate =  encoder.decode( client.getCertificate() );

                if ( content.getSerialNumber() == clientCertificate.getSerialNumber() && !isWaitingForCertificate.syncGet() )
                {
                    LOGGER.log("Renovating invalid certificate: " + clientCertificate.getSerialNumber(),Optional.of(LogTypes.INFO ));

                    CustomCertificate certificate = createCertificate( client.getUsername() );
                    client.setCertificate( askSigneCertificate( certificate ) );
                    sendMessage( new Message( client.getUsername(), "Server",
                            ContentFactory.createLoginRenovateContent( client.getCertificate(), client.getUsername() )
                            ), MSG_SERVER_CONNECTION_OUTPUT
                    );
                }
                else
                {
                    for (ClientUser user : connectedUsers.values() )
                    {
                        if ( encoder.decode(user.getCertificate()).getSerialNumber() == content.getSerialNumber() )
                        {
                            connectedUsers.remove( user.getUsername());
                            LOGGER.log("User '"+user.getUsername()+"' is disconnected from de chat because of revoked certificate", Optional.of(LogTypes.INFO));
                            break;
                        }
                    }
                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                LOGGER.log("Error when decoding certificate." + e.getMessage(), Optional.of(LogTypes.ERROR) );
            }
        }

        private void handleDiffieHellmanMessages( Message message )
        {
            switch ( (DiffieHellmanTypes)message.getContent().getSubType() )
            {
                case KEY_CHANGE -> { this.handleAgreeOnSecret( message ); }

                default -> { throw new RuntimeException("Invalid Message type.");}
            }
        }

        private void handleAgreeOnSecret( Message message )
        {
            DiffieHellmanKeyChangeContent content = (DiffieHellmanKeyChangeContent)message.getContent();
            if( !content.hasValidDigest() )
            {
                LOGGER.log("Received message has invalid digest." , Optional.of(LogTypes.ERROR) );
                return;
            }

            ClientUser sender = connectedUsers.get( message.getSender() );
            if( sender == null)
            {
                LOGGER.log("Received message has invalid sender." , Optional.of(LogTypes.ERROR) );
                return;
            }

            if ( !sender.isAgreeingOnSecret() )
                startAgreeingOnSecret(sender,message);
            else
                endAgreeingOnSecret(sender,message);
        }

        private void startAgreeingOnSecret( ClientUser sender, Message message  )
        {
            try
            {
                //createKey
                sender.setAgreeingOnSecret(true);
                sender.setGeneratedPrivateKey( DiffieHellman.generatePrivateKey() );
                sender.setGeneratedPublicKey( DiffieHellman.generatePublicKey( sender.getGeneratedPrivateKey() ));
                //decrypt DHRSA key
                CustomCertificate fromUserCertificate = new PEMCertificateEncoder().decode( sender.getCertificate() );
                byte[] decryptedDHPublicKey = RSA.decryptRSA( message.getContent().getByteMessage() , fromUserCertificate.getPublicKey() );

                sender.setSharedSecret( DiffieHellman.computeSecret( new BigInteger(decryptedDHPublicKey) , sender.getGeneratedPrivateKey() ) );
                //send DHRSA key
                byte[] encryptedDHPublicKey = RSA.encryptRSA( sender.getGeneratedPublicKey().toByteArray() , clientKeyPair.getPrivate() );
                sendMessage(
                        new Message( client.getUsername(), message.getSender(), ContentFactory.createDiffieHellmanRSAContent( encryptedDHPublicKey ) ),
                        MSG_SERVER_CONNECTION_OUTPUT
                );
            }
            catch (Exception e)
            {
                LOGGER.log("Couldn't decrypt public key: " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        private void endAgreeingOnSecret( ClientUser sender, Message message  )
        {
            try
            {
                CustomCertificate fromUserCertificate = new PEMCertificateEncoder().decode( sender.getCertificate() );
                byte[] decryptedDHPublicKey = RSA.decryptRSA( message.getContent().getByteMessage() , fromUserCertificate.getPublicKey() );
                sender.setSharedSecret( DiffieHellman.computeSecret( new BigInteger(decryptedDHPublicKey) , sender.getGeneratedPrivateKey() ) );
                LOGGER.log( "Secret agreed '" + sender.getSharedSecret() + "' with " + sender.getUsername(), Optional.of(LogTypes.DEBUG) );
                sender.setAgreeingOnSecret(false);
            }
            catch (Exception e)
            {
                LOGGER.log("Couldn't decrypt public key: " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
            finally {
                //signal if main thread is waiting for secret;
                AGREE_ON_SECRETE_LOCK.lock();
                AGREE_ON_SECRETE_LOCK_CONDITION.signal();
                AGREE_ON_SECRETE_LOCK.unlock();
            }
        }

        private void userLogin( LogInContent content )
        {
            try
            {
                ClientUser user = new ClientUser( content.getUSERNAME() );
                user.setCertificate( content.getCertificate() );
                CustomCertificate certificate = new PEMCertificateEncoder().decode( user.getCertificate() );

                if( isValidUserCertificate( user , certificate ) )
                {
                    connectedUsers.put( user.getUsername() , user);
                    LOGGER.log( "The user '" + user.getUsername() + "' has been connected.", Optional.of(LogTypes.INFO));
                }
                else
                    sendInvalidCertificateMessage(user,certificate);
            }
            catch(IOException | ClassNotFoundException e )
            {
                LOGGER.log(e.getMessage()+ " when decoding certificate.", Optional.of(LogTypes.ERROR) );
            }

        }
        private void userLogout( LogOutContent content )
        {

            if(content.hasValidDigest())
            {
                ClientUser user = connectedUsers.remove( content.getStringMessage() );

                if( user != null)
                    LOGGER.log("User '" + user.getUsername() + "' has disconnected.",Optional.of(LogTypes.INFO));
            }
            else
                LOGGER.log( "Received logout message has invalid digest.",Optional.of(LogTypes.ERROR) );
        }
    }
}
