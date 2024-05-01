package Networks;

import Utils.Concurrency.VarSync;
import Utils.Config.Config;
import Utils.Message.Contents.ContentFactory;
import Utils.Message.Contents.ErrorContent;
import Utils.Message.Contents.LogInContent;
import Utils.Message.Contents.RegisterContent;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server extends Thread
{

    private final ServerSocket SOCKET;
    private VarSync<Boolean> isServerRunning;
    private ConcurrentHashMap< String, VarSync<ClientHandler> > connectedUsers;
    private VarSync< ArrayList<ClientHandler> > activeClientHandler;

    public Server(Config config) throws IOException
    {
        SOCKET = new ServerSocket( config.getServerPort() );
        connectedUsers = new ConcurrentHashMap<>();
        activeClientHandler = new VarSync<>( new ArrayList<>(5) );
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
        Socket client;
        while( isServerRunning.syncGet() )
        {
            try {
                client =  SOCKET.accept();
                activeClientHandler.lock();
                activeClientHandler.asyncGet().add( new ClientHandler( client ) );
                activeClientHandler.unlock();

            } catch (IOException e) {
                throw new RuntimeException(e);
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

        public ClientHandler( Socket clientConnection ) throws IOException
        {
            CLIENT_CONNECTION = clientConnection;
            CLIENT_INPUT_STREAM = new ObjectInputStream( CLIENT_CONNECTION.getInputStream ( ) );
            CLIENT_OUTPUT_STREAM = new ObjectOutputStream( CLIENT_CONNECTION.getOutputStream ( ) );
            isRunning = new VarSync<>(false);
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
                throw new RuntimeException( e.getMessage() );
            }
        }

        private void HandleMessage( Message message ){

            switch ( message.getContent().getType() )
            {
                case ACCOUNT -> { HandleAccountMessages( message ); }

                case ERROR -> { /*coms*/ }

                default -> { RedirectMessage( message ); }
            }

        }

        private void HandleAccountMessages ( Message message ){

            switch( (AccountMessageTypes)message.getContent().getSubType() )
            {
                case REGISTER -> { register( (RegisterContent)message.getContent() );  }

                case LOGIN -> { logIn( (LogInContent) message.getContent() ); }

                case LOGOUT -> { logOut() ; }

                default -> { throw new RuntimeException("InvalidContentType"); /*TODO:log*/ }

            }

        }

        private void RedirectMessage ( Message message ){

            if ( message.getRecipient().isBlank() )
            {
                    broadcast( message );
            }
            else
            {


                ErrorContent errorContent = (ErrorContent)ContentFactory.createErrorContent( message.getContent() ,"Invalid recipient");
                /* TODO:log */
                new Message( "Server", user.getUsername() , errorContent);
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
                    connectedUser.lock();
                    connectedUser.asyncGet().CLIENT_OUTPUT_STREAM.writeObject( message );
                    connectedUser.unlock();
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException( e );
                /*TODO:log*/
            }
        }

        private void sendDirectMessage( Message message )
        {
            try
            {
                VarSync<ClientHandler> connectedUser = connectedUsers.get( message.getRecipient() );

                connectedUser.lock();
                connectedUser.asyncGet().CLIENT_OUTPUT_STREAM.writeObject( message );
                connectedUser.unlock();

            }
            catch (Exception e)
            {
                throw new RuntimeException( e );
                /*TODO:log*/
            }
        }

        /*TODO:ADD Integrity to digest*/
        private void register( RegisterContent content )
        {
            user = new User( content.getStringMessage() );
        }

        private void logIn( LogInContent content )
        {
            if(user != null)
            {
                user.setCertificate( content.getCertificate() );
                connectedUsers.put( user.getUsername() , new VarSync<>(this) );

                LogInContent sendContent = new LogInContent( user.getCertificate() , user.getUsername() );

                broadcast( new Message( "Server", "", sendContent ) );
            }
            else
            {

                ErrorContent sendContent = (ErrorContent)ContentFactory.createErrorContent( content,"User is not registered ");
                /*TODO:log*/
                try
                {
                    CLIENT_OUTPUT_STREAM.writeObject( new Message( "Server", "Sender", sendContent ) );
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
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
                /*TODO:LOG*/
            }
        }

    }

}
