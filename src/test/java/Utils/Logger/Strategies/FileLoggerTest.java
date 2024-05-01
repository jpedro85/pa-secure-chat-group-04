package Utils.Logger.Strategies;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.Optional;

class FileLoggerTest {

    private static final String TEST_FILE_PATH = "testFile.log";
    private FileLogger logger;

    @BeforeEach
    void setUp() {
        logger = new FileLogger(TEST_FILE_PATH);
    }

    @AfterEach
    void tearDown() throws IOException {
        logger.close();
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    @Test
    void testLogWritesToFile() throws IOException, InterruptedException {
        String testMessage = "Hello World!";
        logger.log(testMessage, Optional.empty());

        Thread.sleep(100);

        String fileContent = Files.readString(Paths.get(TEST_FILE_PATH));
        Assertions.assertTrue(fileContent.contains(testMessage + "\n"));
    }
}