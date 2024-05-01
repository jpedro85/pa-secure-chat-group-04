package Utils.Certificate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;


public class PEMCertificateEncoder implements CertificateEncoder {

    private static final String HEADER = "-----BEGIN CUSTOM CERTIFICATE-----";
    private static final String FOOTER = "-----END CUSTOM CERTIFICATE-----";

    @Override
    public String encode(CustomCertificate certificate) throws IOException {

        if (certificate == null) {
            throw new IllegalArgumentException("Certificate cannot be null.");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {

            objectOutputStream.writeObject(certificate);
            objectOutputStream.flush();
            String base64Encoded = Base64.getEncoder().encodeToString(outputStream.toByteArray());

            return formatPEM(base64Encoded);
        }

    }

    @Override
    public CustomCertificate decode(String pemData) throws IOException, ClassNotFoundException {
        if (pemData == null || !pemData.contains(HEADER) || !pemData.contains(FOOTER)) {
            throw new IllegalArgumentException("Invalid PEM data provided.");
        }

        String base64Encoded = extractBase64Data(pemData);
        byte[] certBytes = Base64.getDecoder().decode(base64Encoded);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(certBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            return (CustomCertificate) objectInputStream.readObject();
        }
    }

    private String formatPEM(String base64Encoded) {
        return HEADER + "\n" + base64Encoded + "\n" + FOOTER;
    }

    private String extractBase64Data(String pemData) {
        return pemData.replace(HEADER, "").replace(FOOTER, "").trim();
    }
}
