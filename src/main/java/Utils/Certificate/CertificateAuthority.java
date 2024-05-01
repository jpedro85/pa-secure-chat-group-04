package Utils.Certificate;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The CertificateAuthority class represents a simple Certificate Authority server.
 * It can verify, approve, or revoke certificates.
 */
public class CertificateAuthority {

    private static final int PORT = 12345;
    private List<CertificateEntry> certificateEntries;

    /**
     * Constructs a CertificateAuthority object.
     */
    public CertificateAuthority() {
        this.certificateEntries = new ArrayList<>();
    }

    /**
     * Starts the Certificate Authority server and listens for client connections.
     */
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Certificate Authority started. Listening on port " + PORT + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles a client request.
     *
     * @param clientSocket The socket connected to the client.
     */
    private void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Read client request
            String[] requestParts = in.readLine().split(" ");
            String command = requestParts[0];
            String certificateName = requestParts[1];
            // Process request
            String response;
            switch (command) {
                case "VERIFY":
                    response = verifyCertificate(certificateName) ? "APPROVED" : "REVOKED";
                    break;
                case "APPROVE":
                    approveCertificate(certificateName);
                    response = "Certificate approved.";
                    break;
                case "REVOKE":
                    revokeCertificate(certificateName);
                    response = "Certificate revoked.";
                    break;
                default:
                    response = "Invalid command.";
                    break;
            }
            // Send response back to client
            out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies if a certificate is approved.
     *
     * @param certificateName The name of the certificate to verify.
     * @return true if the certificate is approved, false otherwise.
     */
    private boolean verifyCertificate(String certificateName) {
        for (CertificateEntry entry : certificateEntries) {
            if (entry.getName().equals(certificateName)) {
                return entry.isApproved();
            }
        }
        return false;
    }

    /**
     * Approves a certificate.
     *
     * @param certificateName The name of the certificate to approve.
     */
    private void approveCertificate(String certificateName) {
        certificateEntries.add(new CertificateEntry(certificateName, true));
    }

    /**
     * Revokes a certificate.
     *
     * @param certificateName The name of the certificate to revoke.
     */
    private void revokeCertificate(String certificateName) {
        certificateEntries.add(new CertificateEntry(certificateName, false));
    }

    /**
     * Example of using the CertificateAuthority class.
     * This demonstrates how to create a CertificateAuthority object and start the server.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CertificateAuthority ca = new CertificateAuthority();
        ca.start();
    }
}
