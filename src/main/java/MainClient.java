import Networks.Client;
import Networks.Server;
import Utils.Config.Config;
import Utils.Config.ConfigParser;

import Utils.Logger.Logger;
import Utils.Logger.LoggerBuilder;

import java.io.IOException;

public class MainClient {
    public static void main( String[] args )
    {
        Logger logger = createLogger();
        try
        {
            Config config = ConfigParser.getInstance().parseFromIniToConfig("Config.ini");
            Client client = new Client( config ,logger );
            client.start();

        }
        catch (IOException e)
        {
            System.out.println("Can not start program. Failed Loading Config");
        }
    }

    private static Logger createLogger()
    {
        LoggerBuilder builder = new LoggerBuilder();
        return  builder
                .useConsoleLogging()
                .addTimeStamp()
                .addType()
                .build();
    }
}
