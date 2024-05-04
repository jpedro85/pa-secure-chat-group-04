package Networks;

import Utils.Certificate.CertificateEntry;
import Utils.Certificate.CustomCertificate;
import Utils.Certificate.PEMCertificateEncoder;
import Utils.Message.Contents.ContentFactory;
import Utils.Message.Contents.DiffieHellmanContent;
import Utils.Message.Contents.Interfaces.MessageContent;
import Utils.Message.Contents.IntegrityContent;
import Utils.Config.Config;
import Utils.Logger.Enums.LogTypes;
import Utils.Logger.Logger;
import Utils.Message.Contents.TypeContent;
import Utils.Message.EnumTypes.CACommunicationTypes;
import Utils.Message.EnumTypes.DiffieHellmanTypes;
import Utils.Message.Message;
import Utils.Security.DiffieHellman;
import Utils.Security.Encryption.RSA;
import Utils.Security.Integrity.HASH;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The CertificateAuthority class represents a simple Certificate Authority server.
 * It can verify, approve, or revoke certificates.
 */
public class CertificateAuthority extends Server{

    private final Config CONFIG;
    private final Logger LOGGER;
    private ConcurrentHashMap < Integer, CertificateEntry > certificateEntries;
    private final PrivateKey PRIVATE_KEY;
    private final PublicKey PUBLIC_KEY;

    /**
     * Constructs a CertificateAuthority object.
     */
    public CertificateAuthority(Config config, Logger logger)
    {
        super( config.getCaServerPort(), logger );

        CONFIG = config;
        LOGGER = logger;
        this.certificateEntries = new ConcurrentHashMap<>();

        KeyPair pair = RSA.generateKeyPair();
        PRIVATE_KEY = pair.getPrivate();
        PUBLIC_KEY = pair.getPublic();

    }

    @Override
    protected void handleNewConnection(Socket connection) throws IOException
    {
        ClientHandler clientHandler = new CertificateAuthority.ClientHandler( connection );

        currentClientHandlers.lock();
        currentClientHandlers.asyncGet().add( clientHandler );
        currentClientHandlers.unlock();

        clientHandler.start();

    }














    private class ClientHandler extends Server.ClientHandler
    {
        private BigInteger clientDHPublicKey;
        private BigInteger privateDHKey;
        private BigInteger publicDHKey;
        private BigInteger sharedDHSecret = null;

        public ClientHandler( Socket clientConnection ) throws IOException
        {
            super(clientConnection);
        }

        @Override
        protected void handleRequest(Object object)
        {
            LOGGER.log( "inhandle message",Optional.of(LogTypes.DEBUG));
            handleMessage( (Message)object );
        }

        /**
         * Handles a client request.
         *
         * @param message the request of the client.
         */
        private void handleMessage( Message message )
        {
            switch ( message.getContent().getType() )
            {
                case CA_COMMUNICATION -> { handleCaCommunicationMessages( message ) ; }

                case DIFFIE_HELLMAN -> { handleDiffieHellmanMessages( message ) ; }

                case ERROR -> {
                    LOGGER.log( message.getContent().getStringMessage(), Optional.of(LogTypes.ERROR) );
                }

                default -> {
                    LOGGER.log( "Server not prepared for receiving messages of type : " + message.getContent().getType().toString()
                            , Optional.of(LogTypes.ERROR) );
                }
            }
        }

        private void handleDiffieHellmanMessages( Message message )
        {
            switch ( (DiffieHellmanTypes)message.getContent().getSubType() )
            {
                case KEY_CHANGE -> { handleDiffieHellmanKeyChange( (DiffieHellmanContent) message.getContent() ); }

                default -> {
                    LOGGER.log( "Server not prepared for receiving messages of type : " + message.getContent().getType().toString()
                            , Optional.of(LogTypes.ERROR) );
                }
            }
        }

        private void handleDiffieHellmanKeyChange( DiffieHellmanContent content )
        {
            try
            {
                if( content.hasValidDigest() )
                {
                    this.privateDHKey = DiffieHellman.generatePrivateKey();
                    this.publicDHKey = DiffieHellman.generatePublicKey( this.privateDHKey );
                    clientDHPublicKey = content.getPublic_key();
                    CLIENT_OUTPUT_STREAM.writeObject( new Message( "CA", "sender", ContentFactory.createDiffieHellmanContent( publicDHKey )  ) );
                    this.sharedDHSecret = DiffieHellman.computeSecret( clientDHPublicKey, this.privateDHKey );
                }
                else
                {
                    CLIENT_OUTPUT_STREAM.writeObject(
                            new Message( "CA", "sender", ContentFactory.createErrorContent( content , "Received message has not valid digest" )  ) );
                }
            }
            catch (NoSuchAlgorithmException e )
            {
                LOGGER.log( "Algorithm error " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
            catch (IOException e)
            {
                LOGGER.log( "Couldn't send object: " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        private void handleCaCommunicationMessages( Message message )
        {
            switch ( (CACommunicationTypes)message.getContent().getSubType() )
            {
                case SIGNE -> { handleSigneContent( (IntegrityContent)message.getContent(), message.getSender() ); }

                case REVOKE -> { handleRevokeContent( (IntegrityContent)message.getContent(), message.getSender()  ); }

                case CERTIFICATE_STATE -> { handleCheckCertificateContent( (IntegrityContent)message.getContent(), message.getSender() ); }

                case PUBLIC_KEY -> { handlePublicKeyRequest( (TypeContent) message.getContent() , message.getSender() ); }

                default -> {
                    LOGGER.log( "Server not prepared for receiving messages of type : " + message.getContent().getType().toString()
                            , Optional.of(LogTypes.ERROR) );
                }
            }
        }

        private void handleSigneContent( IntegrityContent content, String sender )
        {
            try
            {
                if( sharedDHSecret == null)
                {
                    CLIENT_OUTPUT_STREAM.writeObject( new Message("CA", sender, ContentFactory.createErrorContent(content, "Invalid request:")));
                    LOGGER.log("Client tried to signe without agreeing on a secret.", Optional.of(LogTypes.WARN));
                }
                else if ( content.hasValidMAC( sharedDHSecret.toByteArray() ) )
                {
                    signeCertificate( content , sender );
                }
                else
                {
                    CLIENT_OUTPUT_STREAM.writeObject( new Message( "CA", sender,  ContentFactory.createErrorContent( content , "Received message has not valid digest" )  ) );
                    LOGGER.log("Received message has not valid digest", Optional.of(LogTypes.WARN));
                }
            }
            catch (IOException e)
            {
                LOGGER.log( "Couldn't send object: " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
            catch (ClassNotFoundException e)
            {
                LOGGER.log( "Couldn't convert object: " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        private void handleRevokeContent( IntegrityContent content, String sender )
        {
            try
            {
                if( sharedDHSecret == null)
                {
                    CLIENT_OUTPUT_STREAM.writeObject( new Message("CA", sender, ContentFactory.createErrorContent(content, "Invalid request:")));
                    LOGGER.log("Client tried to signe without agreeing on a secret.", Optional.of(LogTypes.WARN));
                }
                else if ( content.hasValidMAC( sharedDHSecret.toByteArray() ) )
                {
                    revokeCertificate( Integer.parseInt( content.getStringMessage() ) );
                }
                else
                {
                    CLIENT_OUTPUT_STREAM.writeObject( new Message( "CA", sender,  ContentFactory.createErrorContent( content , "Received message has not valid digest" )  ) );
                    LOGGER.log("Received message has not valid digest", Optional.of(LogTypes.WARN));
                }
            }
            catch (IOException e)
            {
                LOGGER.log( "Couldn't send object: " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        private void handleCheckCertificateContent( IntegrityContent content , String sender)
        {
            try
            {
                if( sharedDHSecret == null)
                {
                    CLIENT_OUTPUT_STREAM.writeObject( new Message("CA", sender, ContentFactory.createErrorContent(content, "Invalid request:")));
                    LOGGER.log("Client tried to signe without agreeing on a secret.", Optional.of(LogTypes.WARN));
                    return;
                }
                else if ( !content.hasValidMAC( sharedDHSecret.toByteArray() ) )
                {
                    CLIENT_OUTPUT_STREAM.writeObject( new Message( "CA", sender,  ContentFactory.createErrorContent( content , "Received message has not valid digest" )  ) );
                    LOGGER.log("Received message has not valid digest", Optional.of(LogTypes.WARN));
                    return;
                }

                int serialNumber =  Integer.parseInt( content.getStringMessage() );
                MessageContent isRevokeContent = ContentFactory.createIntegrityContent(
                                                    Boolean.toString( isRevokedCertificate( serialNumber ) ),
                                                    sharedDHSecret,
                                                    CACommunicationTypes.CERTIFICATE_STATE  );

                CLIENT_OUTPUT_STREAM.writeObject( new Message( "CA", sender, isRevokeContent) );
            }
            catch (IOException e)
            {
                LOGGER.log( "Couldn't send object: " + e.getMessage() , Optional.of(LogTypes.ERROR) );
            }
        }

        private void handlePublicKeyRequest( TypeContent content, String sender)
        {
            try
            {
                MessageContent publicKeyContent = ContentFactory.createPublicKeyContent( PUBLIC_KEY, sharedDHSecret );
                CLIENT_OUTPUT_STREAM.writeObject( new Message( "CA" , sender, publicKeyContent ));
            }
            catch (IOException e)
            {
                LOGGER.log( e.getMessage(), Optional.of(LogTypes.ERROR));
            }
        }

        private CustomCertificate extractCertificate( String fileName , PEMCertificateEncoder encoder) throws IOException, ClassNotFoundException
        {
            Path path = Path.of( "src/data/Certificates/" + fileName );
            String fileContent = Files.readString(  path );
            Files.delete( path);
            return encoder.decode(  fileContent ) ;
        }

        private void signeCertificate( MessageContent content, String sender ) throws IOException, ClassNotFoundException
        {
            PEMCertificateEncoder encoder = new PEMCertificateEncoder();
            CustomCertificate certificate = extractCertificate( content.getStringMessage() , encoder );

            if ( !verifyCertificate( certificate ) )
            {
                MessageContent errorContent = ContentFactory.createErrorContent( content , "The certificate has not pass the validate check.");
                CLIENT_OUTPUT_STREAM.writeObject( new Message("CA", sender, errorContent ) );
                return;
            }

            certificate.setIssuer("CA");
            certificate.setValidFrom( new Date() );
            certificate.setValidTo( new Date(System.currentTimeMillis() + CONFIG.getCertificateValidityPeriod() * 1000L) );

            byte[] digest = HASH.generateDigest( certificate.getCertificateData() );
            certificate.setSignature( RSA.encryptRSA( digest, PRIVATE_KEY ) );
            MessageContent signedContent = ContentFactory.createSigneContent( encoder.encode( certificate ) ,sharedDHSecret );
            CLIENT_OUTPUT_STREAM.writeObject( new Message("CA", certificate.getSubject(), signedContent ) );

        }

        /**
         * Approves a certificate if the issuer name does not contain "hacker". An
         *
         * @param certificate The certificate to approve.
         * @return <b>True</b> if the certificate is invalid.
         */
        private boolean verifyCertificate( CustomCertificate certificate )
        {
            CertificateEntry entry =  new CertificateEntry( certificate, !certificate.getSubject().contains("hacker") );
            certificateEntries.put( certificate.getSerialNumber() , entry );
            return  entry.isApproved() ;
        }

        /**
         * Revokes a certificate.
         *
         * @param serialNumber The serialNumber of the certificate to revoke.
         */
        private void revokeCertificate( int serialNumber)
        {
            certificateEntries.get( serialNumber ).revoke();
        }

        private boolean isRevokedCertificate( int serialNumber )
        {
            return !certificateEntries.get( serialNumber ).isApproved();
        }

    }
}