package Networks;

import Utils.Concurrency.VarSync;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Abstract class representing a server.
 */
public abstract class Server extends Thread
{
    /**the port to host the server.*/
    protected final int PORT;

    /**the socket that the clients can connect.*/
    protected ServerSocket socket;

    /**the socket that the clients can connect.*/
    protected final Logger LOGGER;

    /**Represents the current state of the server.*/
    protected VarSync<Boolean> isRunning;

    /**To keep track of all current clients connected.*/
    protected VarSync<ArrayList< ClientHandler >> currentClientHandlers;

    /**
     * Constructs a new server with the specified port and logger.
     *
     * @param port   The port to listen on.
     * @param logger The logger for logging server events.
     */
    public Server (int port, Logger logger)
    {
        PORT = port;
        LOGGER = logger;
        isRunning = new VarSync<>( false );
        currentClientHandlers = new VarSync<>( new ArrayList<>() );
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

        try (ServerSocket socket = new ServerSocket( PORT ))
        {
            this.socket = socket;
            try
            {
                LOGGER.log("Server started listening !", Optional.of(LogTypes.INFO) );
                Socket client;
                do
                {
                    client = socket.accept();
                    LOGGER.log( "Client Connected", Optional.of(LogTypes.INFO) );
                    handleNewConnection( client );
                }
                while (isRunning.syncGet());
            }
            catch (IOException e)
            {
                LOGGER.log("Client disconnected reason: " + e.getMessage(), Optional.of(LogTypes.WARN) );
            }
        }
        catch (IOException e)
        {
            LOGGER.log("Server stopped listening !" + e.getMessage(), Optional.of(LogTypes.INFO) );
        }

        LOGGER.log("Server is closed !", Optional.of(LogTypes.INFO) );
    }

    /**
     * This method is the first handler of a connection. It is called always after an accept.
     *
     * @param connection the accepted connection.
     */
    protected abstract void handleNewConnection( Socket connection ) throws IOException ;

    /**
     * Terminates the server, waiting for all client handles to close.
     */
    public void close() throws InterruptedException, IOException
    {
        LOGGER.log("Server is closing !", Optional.of(LogTypes.INFO) );

        isRunning.syncSet(false);

        currentClientHandlers.lock();

        while( !currentClientHandlers.asyncGet().isEmpty() )
        {
            ClientHandler handler = currentClientHandlers.asyncGet().remove(0);
            currentClientHandlers.unlock();
            handler.close();
            synchronized (handler) {
                while (handler.isAlive()) {
                    handler.wait();
                }
            }
            LOGGER.log("Client connection closed !", Optional.of(LogTypes.INFO) );
            currentClientHandlers.lock();
        }

        currentClientHandlers.unlock();

        if( this.socket != null && !socket.isClosed() )
            this.socket.close();

    }


    /**
     * Represents a client connection, when running is listening for requests.
     */
    protected abstract class ClientHandler extends Thread
    {
        /**The connection to the client.*/
        protected final Socket CLIENT_CONNECTION;
        /**The connection inputStream to the client.*/
        protected final ObjectInputStream CLIENT_INPUT_STREAM;
        /**The connection outputStream to the client.*/
        protected final ObjectOutputStream CLIENT_OUTPUT_STREAM;
        /**The state of the handler.*/
        protected VarSync<Boolean> isRunning;

        /**
         * Constructs a new Client handle.
         *
         * @param clientConnection the connection to the client.
         */
        public ClientHandler( Socket clientConnection )
        {
            try
            {
                CLIENT_CONNECTION = clientConnection;
                CLIENT_INPUT_STREAM = new ObjectInputStream( CLIENT_CONNECTION.getInputStream ( ) );
                CLIENT_OUTPUT_STREAM = new ObjectOutputStream( CLIENT_CONNECTION.getOutputStream ( ) );
                this.isRunning = new VarSync<>(false);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Can not create ClientHandler");
            }
        }

        @Override
        public void start()
        {
            this.isRunning.syncSet(true);
            super.start();
        }

        @Override
        public void run()
        {
            try
            {
                do
                {
                    handleRequest( CLIENT_INPUT_STREAM.readObject() );
                }
                while ( isRunning.syncGet() );
            }
            catch( IOException e )
            {
                LOGGER.log( "Connection Lost to client ! " + e.getMessage(), Optional.of(LogTypes.WARN) );
                currentClientHandlers.lock();
                currentClientHandlers.asyncGet().remove( this );
                currentClientHandlers.unlock();
            }
            catch (ClassNotFoundException e)
            {
                LOGGER.log( e.getMessage(), Optional.of(LogTypes.ERROR) );
            }


        }

        /**
         * Terminates the client handler execution.
         */
        public void close()
        {
            ClientHandler clientHandler;
            this.isRunning.syncSet(false);
            currentClientHandlers.lock();
            currentClientHandlers.asyncGet().remove( this );
            currentClientHandlers.unlock();
            try
            {
                CLIENT_CONNECTION.close();
            }
            catch (IOException e )
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.WARN) );
            }
            catch (Exception e)
            {
                LOGGER.log( e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        /**
         * The firs method called after reading an object (request) from the client objectInputStream
         *
         * @param object the reade object from the objectInputStream.
         */
        protected abstract void handleRequest( Object object );

    }
}
