package Utils.Config;

import java.io.IOException;
import org.ini4j.Ini;

/**
 * Singleton class responsible for parsing configuration settings from INI files
 * into {@link Config} objects. This parser utilizes an {@link IniFileReader}
 * for reading the INI file and then extracts and converts the necessary
 * configuration parameters into the appropriate data types for the
 * {@link Config} object.
 *
 * <p>
 * This class includes validation to ensure that required sections and keys
 * exist within the INI file, throwing exceptions with clear messages if any
 * required configuration is missing or invalid.
 * </p>
 *
 * <p>
 * Usage of this class involves calling the {@link #getInstance()} method to
 * retrieve the singleton instance of the parser, and then using the
 * {@link #parseFromIniToConfig(String)} method to parse a given INI file path
 * into a {@link Config} object.
 * </p>
 *
 * @see Config
 */
public class ConfigParser {

    private static final ConfigParser instance = new ConfigParser();
    private IniFileReader iniFileReader;

    private ConfigParser() {
    }

    /**
     * Parses configuration settings from an INI file located at the given file path
     * into a {@link Config} object.
     *
     * @param filePath The path to the INI file containing the configuration
     *                 settings.
     * @return A populated {@link Config} object with settings extracted from the
     *         INI file.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public Config parseFromIniToConfig(String filePath) throws IOException {
        if (this.iniFileReader == null) {
            this.iniFileReader = IniFileReader.getInstance();
        }
        Ini configFile = this.iniFileReader.readIniFile(filePath);

        Config config = new Config();

        validateSectionExists(configFile, "server");
        config.setServerAmount(parseInteger(configFile, "server", "serverAmount"));
        config.setTaskPoolSize(parseInteger(configFile, "server", "taskPoolSize"));
        config.setStartPort(parseInteger(configFile, "server", "startPort"));
        config.setMaxServersNumber(parseInteger(configFile, "server", "maxServers"));

        validateSectionExists(configFile, "image");
        config.setColumns(parseInteger(configFile, "image", "columns"));
        config.setRows(parseInteger(configFile, "image", "rows"));

        return config;
    }

    /**
     * Ensures that a specified section exists within the INI configuration file,
     * throwing an IllegalArgumentException if the section is missing.
     *
     * @param configFile The INI configuration file.
     * @param section    The section name to validate.
     */
    private void validateSectionExists(Ini configFile, String section) {
        if (!configFile.containsKey(section)) {
            throw new IllegalArgumentException("Missing [" + section + "] section in INI file.");
        }
    }

    /**
     * Parses an integer value from a specified section and key within the INI file.
     *
     * @param configFile The INI configuration file.
     * @param section    The section from which to parse the integer.
     * @param key        The key corresponding to the integer value.
     * @return The parsed integer value.
     * @throws IllegalArgumentException If the key is missing or the value is not a
     *                                  valid integer.
     */
    private int parseInteger(Ini configFile, String section, String key) {
        String valueStr = configFile.get(section, key);
        if (valueStr == null) {
            throw new IllegalArgumentException("Missing key '" + key + "' in [" + section + "] section.");
        }
        try {
            return Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format for '" + key + "' in [" + section + "] section.");
        }
    }

    /**
     * Retrieves the singleton instance of the {@code ConfigParser}.
     *
     * @return The singleton instance of the ConfigParser.
     */
    public static ConfigParser getInstance() {
        return instance;
    }

    /**
     * Allows for the injection of a mock or alternative {@link IniFileReader},
     * facilitating testing or customized file reading strategies.
     *
     * @param iniFileReader The {@link IniFileReader} to use for reading INI files.
     */
    public void setIniFileReader(IniFileReader iniFileReader) {
        this.iniFileReader = iniFileReader;
    }
}
