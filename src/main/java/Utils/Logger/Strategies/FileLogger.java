package Utils.Logger.Strategies;

import Utils.Logger.Logger;
import Utils.Logger.Enums.LogTypes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A logger implementation that logs messages to a file asynchronously.
 * This logger uses a separate logging thread to handle writing messages to the specified file,
 * enabling non-blocking logging operations in multithreaded environments. Messages are queued
 * and written to the file in the order they are received.
 *
 * The logger ensures that all messages are flushed to the file on shutdown by registering
 * a shutdown hook. It implements {@link AutoCloseable} to allow for explicit resource management.
 *
 * Usage example:
 * try (Logger logger = new FileLogger("log.txt")) {
 *     logger.log("An example log message", Optional.empty());
 * }
 */
public class FileLogger implements Logger, AutoCloseable {
    private final String filePath;
    private final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean isDisposed = new AtomicBoolean(false);
    private final Thread loggingThread;

    /**
     * Constructs a new FileLogger that logs messages to the specified file path.
     * Initializes a background thread to process the log messages asynchronously.
     *
     * @param filePath The path to the file where log messages will be written.
     */
    public FileLogger(String filePath) {
        this.filePath = filePath;
        loggingThread = new Thread(this::processLogQueue);
        loggingThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    /**
     * Logs a message to the file. The actual writing is handled asynchronously by the logging thread.
     * Each message is appended with a newline character before being enqueued.
     *
     * @param message The message string to be logged. Should not be null.
     * @param type The optional type of the log, which is not utilized in the current implementation
     *             but could be integrated in future enhancements.
     */
    @Override
    public void log(String message, Optional<LogTypes> type) {
        try {
            logQueue.put(message + "\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Processes the log queue, writing each message to the file as it is received.
     * Continues processing until the logger is disposed or interrupted.
     */
    private void processLogQueue() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            while (!isDisposed.get() || !logQueue.isEmpty()) {
                writer.write(logQueue.take());
                writer.flush();
            }
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            if(e.getMessage() != null)
                System.out.println("Cant start fileLogger. " + e.getMessage());
        }
    }

    /**
     * Closes the logger, ensuring all pending log messages are written to the file
     * and the logging thread is properly shutdown. This method should be called to
     * release resources and cleanly shutdown the logging process.
     */
    @Override
    public void close() {
        isDisposed.set(true);
        loggingThread.interrupt();
        try {
            loggingThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
