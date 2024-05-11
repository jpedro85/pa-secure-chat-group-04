import Networks.CertificateAuthority;
import Networks.Server;
import Utils.Config.Config;
import Utils.Config.ConfigParser;
import Utils.Logger.Logger;
import Utils.Logger.LoggerBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            System.out.println("Terminating" );
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
                .useFileLogging("CA_log "+ new SimpleDateFormat("dd_MM_yy HH_mm_ss").format(new Date()) +".txt")
                .asPlainText()
                .addTimeStamp()
                .addType()
                .build();

    }
}
