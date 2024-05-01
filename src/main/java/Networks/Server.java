package Networks;

import Utils.Concurrency.VarSync;
import Utils.Config.Config;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;
import Utils.Message.Contents.*;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread
{

    private final ServerSocket SOCKET;
    private final Logger LOGGER;
    private VarSync<Boolean> isServerRunning;
    private ConcurrentHashMap< String, VarSync<ClientHandler> > connectedUsers;
    private VarSync< ArrayList<ClientHandler> > activeClientHandler;
    private VarSync< ArrayList<String> > registeredUsernames;

    public Server(Config config , Logger logger) throws IOException
    {
        LOGGER = logger;
        SOCKET = new ServerSocket( config.getMsgServerPort() );
        connectedUsers = new ConcurrentHashMap<>();
        activeClientHandler = new VarSync<>( new ArrayList<>(5) );
        registeredUsernames = new VarSync<>( new ArrayList<>(5) );
        isServerRunning = new VarSync<>(false);
    }

    @Override
    public void start()
    {
        isServerRunning.syncSet(true);
        super.start();
    }

    @Override
    public void run()
    {
        LOGGER.log("Server started listening!", Optional.of(LogTypes.INFO) );
        Socket client;
        while( isServerRunning.syncGet() )
        {
            try
            {
                client =  SOCKET.accept();
                LOGGER.log( "Client Connected", Optional.of(LogTypes.INFO) );
                ClientHandler clientHandler = new ClientHandler( client );
                activeClientHandler.lock();
                activeClientHandler.asyncGet().add( clientHandler );
                activeClientHandler.unlock();
                clientHandler.start();
            }
            catch (IOException e)
            {
                LOGGER.log("Client disconnected reason: " + e.getMessage(), Optional.of(LogTypes.WARN) );
            }
        }
    }

    public void close() throws Exception
    {
        activeClientHandler.lock();
        while( !activeClientHandler.asyncGet().isEmpty() )
        {
            ClientHandler handler = activeClientHandler.asyncGet().remove(0);
            handler.logOut();
            handler.wait();
        }
        activeClientHandler.unlock();

        isServerRunning.syncSet(false);
        SOCKET.close();
    }







    private class ClientHandler extends Thread
    {
        private final Socket CLIENT_CONNECTION;
        private final ObjectInputStream CLIENT_INPUT_STREAM;
        private final ObjectOutputStream CLIENT_OUTPUT_STREAM;
        private User user = null;

        private VarSync<Boolean> isRunning;

        private final VarSync<ClientHandler> LOCK;

        public ClientHandler( Socket clientConnection ) throws IOException
        {
            CLIENT_CONNECTION = clientConnection;
            CLIENT_INPUT_STREAM = new ObjectInputStream( CLIENT_CONNECTION.getInputStream ( ) );
            CLIENT_OUTPUT_STREAM = new ObjectOutputStream( CLIENT_CONNECTION.getOutputStream ( ) );
            isRunning = new VarSync<>(false);
            LOCK = new VarSync<>(this);
        }

        @Override
        public void start()
        {
            isRunning.syncSet(true);
            super.start();
        }
        @Override
        public void run()
        {
            Message message;
            try {

                while ( isRunning.syncGet() )
                {
                    message = (Message)CLIENT_INPUT_STREAM.readObject();
                    HandleMessage(message);
                }

            } catch (Exception e) {
                LOGGER.log("Client disconnected reason: " + e.getMessage(), Optional.of(LogTypes.WARN) );
            }
        }

        private void HandleMessage( Message message ){

            switch ( message.getContent().getType() )
            {
                case ACCOUNT -> { HandleAccountMessages( message ); }

                case ERROR -> {
                    System.out.println("ERROR FROM CLIENT");
                }

                default -> { RedirectMessage( message ); }
            }

        }

        private void HandleAccountMessages ( Message message ){

            switch( (AccountMessageTypes)message.getContent().getSubType() )
            {
                case REGISTER -> { register( (RegisterContent)message.getContent() );  }

                case LOGIN -> { logIn( (LogInContent) message.getContent() ); }

                case LOGOUT -> { logOut() ; }

                default -> { throw new RuntimeException("InvalidContentType"); }

            }

        }

        private void RedirectMessage ( Message message ){

            if ( message.getRecipient().isBlank() )
            {
                broadcast( message );
            }
            else
            {
                sendError( message.getContent(), "Invalid recipient" );
            }

        }

        private void broadcast( Message message )
        {
            try
            {
                VarSync<ClientHandler> connectedUser;
                for( Map.Entry< String, VarSync<ClientHandler> > entry : connectedUsers.entrySet()  )
                {
                    if( entry.getKey().equals( user.getUsername() ) )
                        continue;

                    connectedUser = entry.getValue();
                    sendDirectMessage( connectedUser, message );
                }
            }
            catch (Exception e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        private void sendDirectMessage( Message message )
        {
            VarSync<ClientHandler> connectedUser = connectedUsers.get( message.getRecipient() );
            sendDirectMessage( connectedUser, message );
        }

        private void sendDirectMessage( VarSync<ClientHandler> connectedUser, Message message )
        {
            try
            {

                connectedUser.lock();
                connectedUser.asyncGet().CLIENT_OUTPUT_STREAM.writeObject( message );
                connectedUser.unlock();

            }
            catch (Exception e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        /*TODO:ADD Integrity to digest*/
        private void register( RegisterContent content )
        {
            registeredUsernames.lock();
            if ( registeredUsernames.asyncGet().contains( content.getStringMessage() ) || connectedUsers.containsKey( content.getStringMessage() ) )
            {
                registeredUsernames.unlock();
                sendError( content , "Username already is registered " );
            }
            else
            {
                user = new User( content.getStringMessage() );
                registeredUsernames.asyncGet().add( user.getUsername() );
                registeredUsernames.unlock();
                sendConfirmation( new Message( "Server", user.getUsername(), ContentFactory.createTypeContent( AccountMessageTypes.REGISTER) ) );
            }
        }

        private void logIn( LogInContent content )
        {
            if(user != null)
            {
                user.setCertificate( content.getCertificate() );
                connectedUsers.put( user.getUsername() , LOCK);

                LogInContent sendContent = new LogInContent( user.getCertificate() , user.getUsername() );

                broadcast( new Message( "Server", "", sendContent ) );
                sendConfirmation( new Message( "Server", user.getUsername(), ContentFactory.createTypeContent( AccountMessageTypes.LOGIN) ) );
            }
            else
            {
                sendError( content , "User is not registered" );
            }

        }

        private void sendError(MessageContent content, String error )
        {
            ErrorContent sendContent = (ErrorContent)ContentFactory.createErrorContent( content, error );
            LOGGER.log( error , Optional.of(LogTypes.ERROR) );
            try
            {
                LOCK.lock();
                CLIENT_OUTPUT_STREAM.writeObject( new Message( "Server", "Sender", sendContent ) );
                LOCK.unlock();
            }
            catch (IOException e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        private void sendConfirmation( Message message )
        {
            try
            {
                LOCK.lock();
                CLIENT_OUTPUT_STREAM.writeObject( message );
                LOCK.unlock();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        public void logOut()
        {
            try
            {
                connectedUsers.remove( user.getUsername() );

                activeClientHandler.lock();
                activeClientHandler.asyncGet().remove( this );
                activeClientHandler.unlock();

                CLIENT_CONNECTION.close();
                CLIENT_INPUT_STREAM.close();
                CLIENT_OUTPUT_STREAM.close();

                isRunning.syncSet(false);

            }
            catch (Exception e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

    }

}
