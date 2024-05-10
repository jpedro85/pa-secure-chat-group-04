
import Networks.MSGServer;
import Networks.Server;
import Utils.Config.Config;
import Utils.Config.ConfigParser;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;
import Utils.Logger.LoggerBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TransferQueue;

public class MainMessageServer {

    public static void main( String[] args )
    {
        Logger logger = createLogger();
        try
        {
            Config config = ConfigParser.getInstance().parseFromIniToConfig("Config.ini");
            MSGServer server = new MSGServer( config ,logger );
            server.start();

            Runtime.getRuntime().addShutdownHook( new Thread(() -> {
                cleanUp(server);
            }));

            synchronized ( server )
            {
                while ( server.isAlive() ) server.wait();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Can not start program." + e.getMessage() );
        }

        logger.log( "Shuting down!" , Optional.of(LogTypes.INFO) );
    }

    private static Logger createLogger()
    {
        LoggerBuilder builder = new LoggerBuilder();
        return  builder
                .useConsoleLogging()
                .addTimeStamp()
                .addType()
                .useFileLogging("log.txt")
                .asPlainText()
                .addTimeStamp()
                .addType()
                .build();

    }

    private static void cleanUp( Server server )
    {
        try
        {
            System.out.println("Can not start program." );
            server.close();
            synchronized ( server )
            {
                while ( server.isAlive() ) server.wait();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
