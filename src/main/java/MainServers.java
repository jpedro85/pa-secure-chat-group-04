
import Networks.Server;
import Utils.Config.Config;
import Utils.Config.ConfigParser;
import Utils.Logger.Logger;
import Utils.Logger.LoggerBuilder;

import java.io.IOException;

public class MainServers {

    public static void main( String[] args )
    {
        Logger logger = createLogger();
        try
        {
            Config config = ConfigParser.getInstance().parseFromIniToConfig("Config.ini");
            Server server = new Server( config ,logger );
            server.start();
        }
        catch (IOException e)
        {
            System.out.println("Can not start program." + e.getMessage() );
        }
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

}
