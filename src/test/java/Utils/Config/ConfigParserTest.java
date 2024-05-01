package Utils.Config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.ini4j.Ini;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConfigParserTest {

    @Mock
    private IniFileReader mockIniFileReader;

    @BeforeEach
    public void setUp() throws IOException {
        mockIniFileReader = Mockito.mock(IniFileReader.class);
    }

    @Test
    public void parseFromIniToConfig_ValidConfig_ReturnsConfigObject() throws IOException {

        Ini mockIni = new Ini();
        mockIni.put("ports", "ServerCAPort", "1025");
        mockIni.put("ports", "ServerMSGPort", "1024");

        mockIni.put("certificate", "CertificateValidityPeriod", "10");
        when(mockIniFileReader.readIniFile("validConfig")).thenReturn(mockIni);

        ConfigParser configParser = ConfigParser.getInstance();
        configParser.setIniFileReader(mockIniFileReader);

        Config result = configParser.parseFromIniToConfig("validConfig");

        assertNotNull(result);
        assertInstanceOf(Config.class, result);
        assertEquals(1025, result.getCaServerPort());
        assertEquals(1024, result.getMsgServerPort());
        assertEquals(10, result.getCertificateValidityPeriod());

    }

    @Test
    public void parseFromIniToConfig_MissingSection_ThrowsIllegalArgumentException() throws IOException {
        Ini mockIniMissing = new Ini();

        when(mockIniFileReader.readIniFile("configWithInvalidValue")).thenReturn(mockIniMissing);

        ConfigParser configParser = ConfigParser.getInstance();
        configParser.setIniFileReader(mockIniFileReader);

        assertThrows(IllegalArgumentException.class, () -> configParser.parseFromIniToConfig("configWithInvalidValue"),
                "Expected to throw due to invalid integer value.");
    }

    @AfterAll
    public static void cleanFiles()
    {
        Path logFilePath = Paths.get("invalidFormat.ini");
        try {
            Files.deleteIfExists(logFilePath);
        } catch (IOException e) {
            System.err.println("Failed to delete the log file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
