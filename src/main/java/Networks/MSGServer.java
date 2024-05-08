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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MSGServer extends Server
{
    private ConcurrentHashMap< String, VarSync<ClientHandler> > connectedUsers;
    private VarSync< ArrayList<String> > registeredUsernames;

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








    
    private class ClientHandler extends Server.ClientHandler
    {
        private User user = null;
        private final VarSync<ClientHandler> LOCK;

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

        private void HandleAccountMessages ( Message message ){

            switch( (AccountMessageTypes)message.getContent().getSubType() )
            {
                case REGISTER -> { register( (RegisterContent)message.getContent() );  }

                case LOGIN -> { logIn( (LogInContent) message.getContent() ); }

                case LOGOUT -> { close(); }

                default -> { throw new RuntimeException("InvalidContentType"); }

            }

        }

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
            }
            catch (Exception e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
            finally {
                connectedUser.unlock();
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

                LogInContent sendContent = new LogInContent( user.getCertificate() , user.getUsername() );
                broadcast( new Message( "Server", "", sendContent ) );

                LOGGER.log("User logged in.",Optional.of(LogTypes.INFO));
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

            }
            catch (IOException e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
            finally {
                LOCK.unlock();
            }
        }

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
            LOGGER.log("User logged out.",Optional.of(LogTypes.INFO));
        }
    }

}
