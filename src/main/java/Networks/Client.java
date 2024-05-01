package Networks;

import Utils.Concurrency.VarSync;
import Utils.Config.Config;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;
import Utils.Message.Contents.ContentFactory;
import Utils.Message.Contents.MessageContent;
import Utils.Message.EnumTypes.AccountMessageTypes;
import Utils.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client
{
    private final Logger LOGGER;
    private final Config CONFIG;
    private final Scanner SCANNER;
    private VarSync<ArrayList<User> > connectedUser;
    private BlockingQueue<Message> queue;

    private final Socket MSG_SERVER_CONNECTION;
    private final ObjectInputStream MSG_SERVER_CONNECTION_INPUT;
    private final ObjectOutputStream MSG_SERVER_CONNECTION_OUTPUT;
//    private final Socket CA_SERVER_CONNECTION;
//    private final ObjectInputStream CA_SERVER_CONNECTION_INPUT;
//    private final ObjectOutputStream CA_SERVER_CONNECTION_OUTPUT;

    private User client;

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

//            CA_SERVER_CONNECTION = connect( CONFIG.getCaServerPort() );
//            CA_SERVER_CONNECTION_OUTPUT = new ObjectOutputStream( CA_SERVER_CONNECTION.getOutputStream() );
//            CA_SERVER_CONNECTION_INPUT = new ObjectInputStream( CA_SERVER_CONNECTION.getInputStream() );
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void start()
    {
        register();

        while (true);

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

    }

    private void ListMessages()
    {

    }

    private void sendMessage()
    {

    }

    private void register()
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
                client = new User( username );
                break;
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

    private void certificate()
    {

    }

    private void login()
    {

    }


    private class Receiver
    {

    }





}
