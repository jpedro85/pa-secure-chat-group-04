package Utils.Certificate;

import java.io.IOException;

public interface CertificateEncoder {
    String encode(CustomCertificate certificate) throws IOException;

    CustomCertificate decode(String data) throws IOException, ClassNotFoundException;
}
