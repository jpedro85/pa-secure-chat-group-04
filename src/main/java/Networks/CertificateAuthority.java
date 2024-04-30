package Networks;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CertificateAuthority {

    private static final int PORT = 12345;
    private List<CertificateEntry> certificateEntries;

    public CertificateAuthority() {
        this.certificateEntries = new ArrayList<>();
    }

    public static void main(String[] args) {
        CertificateAuthority ca = new CertificateAuthority();
        ca.start();
    }

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

    private boolean verifyCertificate(String certificateName) {
        for (CertificateEntry entry : certificateEntries) {
            if (entry.getName().equals(certificateName)) {
                return entry.isApproved();
            }
        }
        return false;
    }

    private void approveCertificate(String certificateName) {
        certificateEntries.add(new CertificateEntry(certificateName, true));
    }

    private void revokeCertificate(String certificateName) {
        certificateEntries.add(new CertificateEntry(certificateName, false));
    }
}
