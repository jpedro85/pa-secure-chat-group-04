package Networks;

import Utils.Concurrency.VarSync;
import Utils.Config.Config;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;
import Utils.Message.Contents.*;
import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Represents a messaging server for handling client connections and messages.
 * It is responsible for and new connection, redirecting and broadcast of messages from the connected users.
 */
public class MSGServer extends Server
{
    /**To keep track of all connected users.*/
    private ConcurrentHashMap< String, VarSync<ClientHandler> > connectedUsers;

    /**To keep track of all usernames that are in use.*/
    private VarSync< ArrayList<String> > registeredUsernames;

    /**
     * Constructs a new MSGServer with the given configuration and logger.
     *
     * @param config The configuration for the server.
     * @param logger The logger for logging server events.
     */
    public MSGServer(Config config , Logger logger)
    {
        super( config.getMsgServerPort(), logger );

        connectedUsers = new ConcurrentHashMap<>();
        registeredUsernames = new VarSync<>( new ArrayList<>(5) );
    }

    @Override
    protected void handleNewConnection(Socket connection)
    {
        ClientHandler clientHandler = new ClientHandler( connection );

        currentClientHandlers.lock();
        currentClientHandlers.asyncGet().add(clientHandler);
        currentClientHandlers.unlock();

        clientHandler.start();

    }


    /**
     * Represents a client handler responsible for managing client connections and requests to the MSGServer.
     */
    private class ClientHandler extends Server.ClientHandler
    {

        private User user = null;
        private final VarSync<ClientHandler> LOCK;

        /**
         * Constructs a new client handler with the specified client connection.
         *
         * @param clientConnection The socket representing the client connection.
         */
        public ClientHandler( Socket clientConnection )
        {
            super(clientConnection);
            LOCK = new VarSync<>(this);
        }

        @Override
        protected void handleRequest(Object object)
        {
            handleMessage( (Message)object );
        }

        /**
         * Handles incoming messages from clients. Chooses the appropriate handler for the message.
         *
         * @param message The message received from the client.
         */
        private void handleMessage( Message message ){

            switch ( message.getContent().getType() )
            {
                case ACCOUNT -> { HandleAccountMessages( message ); }

                case ERROR -> {
                    System.out.println("ERROR FROM CLIENT");
                }

                default -> { RedirectMessage( message ); }
            }

        }

        /**
         * Handles account-related messages (e.g., register, login, logout).
         *
         * @param message The account message to handle.
         */
        private void HandleAccountMessages ( Message message ){

            switch( (AccountMessageTypes)message.getContent().getSubType() )
            {
                case REGISTER -> { register( (RegisterContent)message.getContent() );  }

                case LOGIN -> { logIn( (LogInContent) message.getContent() ); }

                case LOGIN_RENOVATE -> { renovateLogin( message ); }

                case LOGOUT -> { close(); }

                default -> { throw new RuntimeException("InvalidContentType"); }

            }

        }

        /**
         * Redirects messages sending directly to a specific recipient or broadcast if message sender is ""
         *
         * @param message The message to redirect.
         */
        private void RedirectMessage ( Message message ){

            if ( message.getRecipient().isBlank() )
            {
                LOGGER.log( String.format( "BroadCasting Msg from %s.", message.getSender() ), Optional.of(LogTypes.INFO));
                broadcast( message );
            }
            else
            {
                LOGGER.log( String.format( "Redirection Msg type:%s from %s to %s", message.getContent().getSubType(), message.getSender() , message.getRecipient() ), Optional.of(LogTypes.INFO));
                sendDirectMessage( message );
            }

        }

        /**
         * Broadcasts the message to all connected users except the user that sent the message.
         *
         * @param message The message to broadcast.
         */
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

        /**
         * Sends a direct message to the message recipient.
         *
         * @param message the message to send.
         */
        private void sendDirectMessage( Message message )
        {
            VarSync<ClientHandler> connectedUser = connectedUsers.get( message.getRecipient() );
            if( connectedUser != null )
                sendDirectMessage( connectedUser, message );
            else
                LOGGER.log("Not redirected!\nReceived redirect request to a not connected user: " + message.getRecipient() , Optional.of(LogTypes.WARN));
        }

        /**
         * Sends a direct message to the message recipient.
         * Acquires the connectedUser user lock before sending.
         *
         * @param message the message to send.
         * @param connectedUser the user to send the message.
         */
        private void sendDirectMessage( VarSync<ClientHandler> connectedUser, Message message )
        {
            try
            {
                connectedUser.lock();
                connectedUser.asyncGet().CLIENT_OUTPUT_STREAM.writeObject( message );
            }
            catch (Exception e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
            finally {
                connectedUser.unlock();
            }
        }

        /**
         * Registers the new username. If already exists sends an error to the client that requested the registering.
         *
         * @param content the content of a message of type Register.
         */
        private void register( RegisterContent content )
        {

            if (!content.hasValidDigest())
            {
                LOGGER.log("Received register request has no valid digest.", Optional.of(LogTypes.ERROR));
                return;
            }

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

        /**
         * Handles the renovate login request.
         *
         * @param message the message to handle.
         */
        private void renovateLogin( Message message )
        {
            if( !((LogInRenovateContent)message.getContent()).hasValidDigest() )
            {
                LOGGER.log("Received message has invalid digest at renovate login " + message.getSender() , Optional.of(LogTypes.ERROR) );
                return;
            }

            user.setCertificate( ((LogInRenovateContent)message.getContent()).getCertificate() );
            MessageContent sendContent = ContentFactory.createLoginRenovateContent( user.getCertificate() , user.getUsername() );
            broadcast( new Message( "Server", "", sendContent ) );

            LOGGER.log("User " + user.getUsername() + " renovated login .",Optional.of(LogTypes.INFO));
        }

        /**
         * Handles the login request. Checks if the user is already logged.
         * If successfully login send a login confirmation message and a message with all current connected users.
         * Finally broadcasts to the information of the recently connected users.
         *
         * @param content the logInContent.
         */
        private void logIn( LogInContent content )
        {
            if (!content.hasValidDigest())
            {
                LOGGER.log("Received login request has no valid digest.", Optional.of(LogTypes.ERROR));
                return;
            }

            if(user != null)
            {
                user.setCertificate( content.getCertificate() );
                connectedUsers.put( user.getUsername() , LOCK);



                ArrayList<User> usersLoggedIN = new ArrayList<>();
                for ( Map.Entry<String , VarSync < ClientHandler > >  entry : connectedUsers.entrySet() )
                {
                    if ( entry.getKey().equals( user.getUsername() ) )
                        continue;

                    entry.getValue().lock();
                    usersLoggedIN.add( entry.getValue().asyncGet().user );
                    entry.getValue().unlock();
                }

                sendConfirmation( new Message( "Server", user.getUsername(), ContentFactory.createTypeContent( AccountMessageTypes.LOGIN) ) );

                sendConfirmation( new Message( "Server", user.getUsername(), ContentFactory.createAllLoggedInContent( usersLoggedIN ) ));

                MessageContent sendContent = ContentFactory.createLoginContent( user.getCertificate() , user.getUsername() );
                broadcast( new Message( "Server", "", sendContent ) );

                LOGGER.log("User " + user.getUsername() +" logged in.",Optional.of(LogTypes.INFO));
            }
            else
            {
                sendError( content , "User is not registered" );
            }

        }

        /**
         * Sends the specified error message to the user.
         *
         * @param content the logInContent.
         */
        private void sendError(MessageContent content, String error )
        {
            ErrorContent sendContent = (ErrorContent)ContentFactory.createErrorContent( content, error );
            LOGGER.log( error , Optional.of(LogTypes.ERROR) );
            try
            {
                LOCK.lock();
                CLIENT_OUTPUT_STREAM.writeObject( new Message( "Server", "Sender", sendContent ) );

            }
            catch (IOException e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
            finally {
                LOCK.unlock();
            }
        }

        /**
         * Sends the specified confirmation message to the user.
         *
         * @param message the confirmation message.
         */
        private void sendConfirmation( Message message )
        {
            try
            {
                LOCK.lock();
                CLIENT_OUTPUT_STREAM.writeObject( message );
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            finally {
                LOCK.unlock();
            }
        }

        /**
         * Handles the logout event disconnecting the user from the server and freeing the username.
         */
        public void logOut()
        {
            if( user != null && connectedUsers.containsKey( user.getUsername() ) )
            {
                connectedUsers.remove( user.getUsername() );
                registeredUsernames.lock();
                registeredUsernames.asyncGet().remove( user.getUsername() );
                registeredUsernames.unlock();
                broadcast(new Message( "Sever", "", ContentFactory.createLogoutContent( user.getUsername() ) ) );
            }

        }

        @Override
        public void close()
        {
            logOut();
            super.close();
            LOGGER.log("User '" + user.getUsername() + "' logged out.",Optional.of(LogTypes.INFO));
        }
    }

}
