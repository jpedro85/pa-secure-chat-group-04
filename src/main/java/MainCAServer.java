import Networks.CertificateAuthority;
import Networks.Server;
import Utils.Config.Config;
import Utils.Config.ConfigParser;
import Utils.Logger.Logger;
import Utils.Logger.LoggerBuilder;

import java.io.IOException;

public class MainCAServer
{
    public static void main( String[] args )
    {
        Logger logger = createLogger();
        try
        {
            Config config = ConfigParser.getInstance().parseFromIniToConfig("Config.ini");
            CertificateAuthority certificateAuthority = new CertificateAuthority( config ,logger );
            certificateAuthority.start();

            Runtime.getRuntime().addShutdownHook( new Thread(() -> {
                // Perform cleanup actions here
                cleanUp(certificateAuthority);
            }));

        }
        catch (IOException e)
        {
            System.out.println("Can not start program." + e.getMessage() );
        }
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
