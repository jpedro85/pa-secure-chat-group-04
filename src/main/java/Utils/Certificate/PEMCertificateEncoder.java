package Utils.Certificate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 * This class provides functionality for encoding and decoding custom certificates
 * into and from a PEM (Privacy Enhanced Mail) format. The PEM format used here is a
 * textual encoding of binary data, specifically for serialized Java objects
 * representing certificates. It includes custom headers and footers to distinguish
 * the encoded data.
 *
 * <p>The encoding process involves serializing a {@link CustomCertificate} object
 * into a byte array, then encoding this byte array into a Base64 string, and finally
 * formatting this string into the custom PEM format with appropriate headers and footers.
 *
 * <p>The decoding process reverses this encoding, extracting the Base64 string from
 * the PEM data, decoding it back into a byte array, and deserializing this array
 * into a {@link CustomCertificate} object.
 *
 * <h2>Example Usage</h2>
 * <p>Here is an example demonstrating how to encode and decode a {@link CustomCertificate}:
 * <pre>
 * {@code
 * // Create a new custom certificate instance (details omitted)
 * CustomCertificate certificate; // Assume its initializes using the generator
 *
 * // Create an instance of the encoder
 * PEMCertificateEncoder encoder = new PEMCertificateEncoder();
 *
 * // Encode the certificate
 * String pemString;
 * try {
 *     pemString = encoder.encode(certificate);
 *     System.out.println("Encoded PEM: " + pemString);
 * } catch (IOException e) {
 *     e.printStackTrace();
 * }
 *
 * // Decode the certificate
 * CustomCertificate decodedCertificate;
 * try {
 *     decodedCertificate = encoder.decode(pemString);
 *     System.out.println("Decoded Certificate Owner: " + decodedCertificate.getOwner());
 * } catch (IOException | ClassNotFoundException e) {
 *     e.printStackTrace();
 * }
 * }
 * </pre>
 *
 * <p>Usage of this class allows for secure and standard transmission or storage of
 * custom certificates in a format that is interoperable with systems and libraries
 * expecting PEM encoded data.
 */
public class PEMCertificateEncoder implements CertificateEncoder {

    private static final String HEADER = "-----BEGIN CUSTOM CERTIFICATE-----";
    private static final String FOOTER = "-----END CUSTOM CERTIFICATE-----";

    /**
     * Encodes the specified {@link CustomCertificate} into a custom PEM format string.
     *
     * @param certificate the {@link CustomCertificate} to encode; must not be null.
     * @return a string representing the PEM encoded certificate.
     * @throws IOException if an I/O error occurs during the encoding process.
     * @throws IllegalArgumentException if the provided certificate is null.
     */
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

    /**
     * Decodes a PEM formatted string into a {@link CustomCertificate}.
     *
     * @param pemData the PEM formatted string to decode; must not be null and must contain the custom headers and footers.
     * @return the decoded {@link CustomCertificate}.
     * @throws IOException if an I/O error occurs during the decoding process.
     * @throws ClassNotFoundException if the class of a serialized object cannot be found during the decoding process.
     * @throws IllegalArgumentException if the provided PEM data is null, or does not contain the proper headers and footers.
     */
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

    /**
     * Formats a Base64 encoded string into a custom PEM format with headers and footers.
     *
     * @param base64Encoded the Base64 encoded string to format.
     * @return the formatted PEM string.
     */
    private String formatPEM(String base64Encoded) {
        return HEADER + "\n" + base64Encoded + "\n" + FOOTER;
    }

    /**
     * Extracts the Base64 encoded data from a custom PEM formatted string, removing headers and footers.
     *
     * @param pemData the PEM formatted string.
     * @return the Base64 encoded data string.
     */
    private String extractBase64Data(String pemData) {
        return pemData.replace(HEADER, "").replace(FOOTER, "").trim();
    }
}
