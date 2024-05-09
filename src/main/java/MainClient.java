
import Networks.Client;
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

            Runtime.getRuntime().addShutdownHook( new Thread ( () -> {
                try
                {
                    client.terminate();
                }
                catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }) );

            client.start();
        }
        catch ( IOException e)
        {
            System.out.println("Can not start program.");
            System.out.println(e.getMessage());
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
