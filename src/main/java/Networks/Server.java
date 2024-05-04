package Networks;

import Utils.Concurrency.VarSync;
import Utils.Config.Config;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

public abstract class Server extends Thread
{
    protected final int PORT;

    protected ServerSocket sokect;
    protected final Logger LOGGER;
    protected VarSync<Boolean> isRunning;
    protected VarSync<ArrayList< ClientHandler >> currentClientHandlers;

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
            this.sokect = socket;
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

    protected abstract void handleNewConnection( Socket connection ) throws IOException ;

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

        if( this.sokect != null && !sokect.isClosed() )
            this.sokect.close();

    }



    protected class ClientHandler extends Thread
    {
        protected final Socket CLIENT_CONNECTION;
        protected final ObjectInputStream CLIENT_INPUT_STREAM;
        protected final ObjectOutputStream CLIENT_OUTPUT_STREAM;
        protected VarSync<Boolean> isRunning;

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

        protected void handleRequest( Object object ){}

    }
}
