package Utils.Certificate;

/**
 * The {@code SerialNumberGenerator} class provides a thread-safe mechanism to generate
 * unique serial numbers. This class is typically used in environments where a unique identifier
 * is required for each certificate or document generated, ensuring no two certificates share
 * the same serial number.
 *
 * <p>It employs a static counter to keep track of the last serial number issued and increments
 * this counter for each call to {@link #getNextSerialNumber()}, which ensures that each serial
 * number is unique across the application's runtime.</p>
 */
class SerialNumberGenerator {
    /**
     * The counter tracking the next available serial number.
     * It starts at 0 and is incremented each time a new serial number is requested.
     */
    private static int serialNumberCount = 0;

    /**
     * Provides the next available serial number. This method is thread-safe to ensure that
     * the serial number it returns is unique across multiple threads accessing this method
     * concurrently.
     *
     * @return the next available unique serial number as an {@code int}.
     */
    public static synchronized int getNextSerialNumber() {
        return serialNumberCount++;
    }
}
