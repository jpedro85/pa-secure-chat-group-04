package Utils.Certificate;

import java.io.IOException;

/**
 * The {@code CertificateEncoder} interface provides the blueprint for implementing methods
 * to encode and decode custom certificate objects. Implementations of this interface are
 * responsible for converting {@link CustomCertificate} objects into a string format for storage
 * or transmission and reconstructing them from the same string format.
 *
 * <p>
 * This interface is crucial for systems that need to persist or exchange certificate data
 * in a format-agnostic way, ensuring that certificates can be stored or transmitted efficiently
 * across different platforms or technologies.
 * </p>
 */
public interface CertificateEncoder {

    /**
     * Encodes a {@link CustomCertificate} into a string representation.
     * This method can be used to serialize a certificate object into a storage-friendly format
     * which can then be stored in a database or sent over a network.
     *
     * @param certificate the {@link CustomCertificate} to be encoded
     * @return a string representation of the certificate
     * @throws IOException if an input/output error occurs during encoding
     */
    String encode(CustomCertificate certificate) throws IOException;

    /**
     * Decodes a string representation of a certificate back into a {@link CustomCertificate} object.
     * This method is used to deserialize a certificate from a format like JSON or XML into a usable
     * {@link CustomCertificate} instance. It is useful for reconstructing certificate data received from
     * storage or network transmission.
     *
     * @param data the string representation of the certificate to be decoded
     * @return a {@link CustomCertificate} reconstructed from the provided string data
     * @throws IOException if an input/output error occurs during decoding
     * @throws ClassNotFoundException if the class of a serialized object cannot be found
     */
    CustomCertificate decode(String data) throws IOException, ClassNotFoundException;
}
