package Utils.Config;

import org.ini4j.Ini;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Singleton class responsible for reading INI files. This class provides a
 * centralized approach to load configuration files from the filesystem,
 * ensuring that INI file reading logic is consistently applied across the
 * application.
 *
 * <p>
 * Utilizing the {@code org.ini4j.Ini} class for parsing, {@code IniFileReader}
 * abstracts the file reading process, making it easier to read and process INI
 * files throughout the application. By implementing the singleton pattern, this
 * class ensures that only a single instance of the file reader is ever created,
 * reducing overhead and simplifying access to file reading capabilities.
 * </p>
 *
 * <p>
 * Users of this class can obtain the singleton instance through the
 * {@link #getInstance()} method and then call {@link #readIniFile(String)} to
 * read a specific INI file by its file path.
 * </p>
 */
public class IniFileReader {

    private static final IniFileReader instance = new IniFileReader();

    private IniFileReader() {
    }

    /**
     * Retrieves the singleton instance of the {@code IniFileReader}.
     *
     * @return The singleton instance of the IniFileReader.
     */
    public static IniFileReader getInstance() {
        return instance;
    }

    /**
     * Reads an INI file from the specified file path and returns an {@link Ini}
     * object representing the contents of the file.
     *
     * @param filePath The path to the INI file to be read.
     * @return An {@link Ini} object containing the data from the INI file.
     * @throws IOException           If an error occurs during file reading.
     * @throws FileNotFoundException If the specified file does not exist.
     */
    public Ini readIniFile(String filePath) throws IOException {
        try {
            File configFile = new File(filePath);
            return new Ini(configFile);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found.");
        }
    }
}