package Utils.Config;

import java.nio.file.Paths;

/**
 * Represents configuration settings for the system, encapsulating various
 * parameters such as server amount, task pool size, and dimensions in which are
 * going to be used to divide the image. This class provides a structured
 * approach to accessing and modifying configuration settings, which are crucial
 * for the system's operation and performance tuning.
 *
 * <p>
 * Instances of this class are typically populated from configuration files or
 * other external data sources at startup, providing a centralized point of
 * access for configuration parameters throughout the system.
 * </p>
 */
public class Config {
    private int msgServerPort;
    private int caServerPort;
    private int certificateValidityPeriod;
    private final String savePath = Paths.get("src", "results").toString();

    /**
     * Creates a new instance of {@code Config} with default values. This
     * constructor initializes a new configuration object ready for setting up the
     * system's operational parameters, such as server amount, task pool size, image
     * division dimensions, and network port configurations.
     *
     * <p>
     * Initial values are set to their defaults (typically zero for integers and
     * {@code null} for objects) and should be explicitly set via the provided
     * setter methods according to system requirements or configuration files.
     * </p>
     *
     * <p>
     * This approach allows for flexible configuration management, enabling the
     * dynamic adjustment of system settings at runtime or as dictated by external
     * sources.
     * </p>
     */
    public Config() {}

    /**
     * Gets the message server port where the server is to be hosted on.
     *
     * @return message server port
     */
    public int getMsgServerPort() {
        return msgServerPort;
    }


    /**
     * Sets the message server port where the server is to be hosted on.
     *
     * @param msgServerPort the message server port to set
     */
    public void setMsgServerPort(int msgServerPort) {
        this.msgServerPort = msgServerPort;
    }

    /**
     * Gets the CA (Certificate Authority) server port.
     *
     * @return the CA server port
     */
    public int getCaServerPort() {
        return caServerPort;
    }

    /**
     * Sets the CA (Certificate Authority) server port.
     *
     * @param caServerPort the CA server port to set
     */
    public void setCaServerPort(int caServerPort) {
        this.caServerPort = caServerPort;
    }

    /**
     * Gets the validity period of certificates in seconds.
     *
     * @return the certificate validity period in seconds
     */
    public int getCertificateValidityPeriod() {
        return certificateValidityPeriod;
    }

    /**
     * Sets the validity period of certificates in seconds.
     *
     * @param certificateValidityPeriod the certificate validity period to set, in seconds
     */
    public void setCertificateValidityPeriod(int certificateValidityPeriod) {
        this.certificateValidityPeriod = certificateValidityPeriod;
    }

    /**
     * Gets the save path that image is going to be saved.
     *
     * @return The save path;
     */
    public String getSavePath() {
        return savePath;
    }
}