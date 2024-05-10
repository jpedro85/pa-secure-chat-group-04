package Utils.Certificate;

class SerialNumberGenerator {
    private static int serialNumberCount = 0;

    public static synchronized int getNextSerialNumber() {
        return serialNumberCount++;
    }
}
