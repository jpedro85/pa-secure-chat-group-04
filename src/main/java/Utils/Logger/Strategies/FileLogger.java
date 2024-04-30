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


public class FileLogger implements Logger, AutoCloseable {
    private String filePath;
    private BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
    private AtomicBoolean isDisposed = new AtomicBoolean(false);
    private Thread loggingThread;

    public FileLogger(String filePath) {
        this.filePath = filePath;
        loggingThread = new Thread(this::processLogQueue);
        loggingThread.start();
    }

    @Override
    public void log(String message, Optional<LogTypes> type) {
        try {
            logQueue.put(message + "\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processLogQueue() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            while (!isDisposed.get() || !logQueue.isEmpty()) {
                writer.write(logQueue.take());
                writer.flush();
            }
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        }
    }

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
