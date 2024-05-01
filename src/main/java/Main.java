import Utils.Logger.Decorator.UpperCaseDecorator;
import Utils.Logger.Enums.LogTypes;
import Utils.Certificate.CertificateGenerator;
import Utils.Certificate.CustomCertificate;
import Utils.Certificate.PEMCertificateEncoder;
import Utils.Logger.Logger;
import Utils.Logger.LoggerBuilder;
import Utils.Logger.Strategies.ConsoleLogger;

import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Date;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws Exception{
        LoggerBuilder builder = new LoggerBuilder();
        Logger logger = builder
                .useConsoleLogging()
                .addTimeStamp()
                .addType()
                .build();
        Logger loggers = new ConsoleLogger();
        Logger decorator = new UpperCaseDecorator(loggers);

        logger.log("An informational message", Optional.of(LogTypes.INFO));
        logger.log("A warning message", Optional.of(LogTypes.WARN));
        logger.log("A debug message", Optional.of(LogTypes.DEBUG));
        logger.log("A Error message", Optional.of(LogTypes.ERROR));

        PEMCertificateEncoder encoder = new PEMCertificateEncoder();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        PublicKey publicKey = keyGen.generateKeyPair().getPublic();
        Date now = new Date();
        Date later = new Date(now.getTime() + 1000000);

        CustomCertificate certificate = new CertificateGenerator()
                .withPublicKey(publicKey)
                .issuedBy("Issuer")
                .forSubject("Subject")
                .validTo(later)
                .validFrom(now)
                .generate();

        String pemEncoded = encoder.encode(certificate);
        System.out.println("Encoded PEM:");
        System.out.println(pemEncoded);

        CustomCertificate decodedCertificate = encoder.decode(pemEncoded);
        System.out.println("Decoded Certificate:");
        System.out.println(decodedCertificate);
    }
}
