package Utils.Logger.Enums;

public enum ColorSchemes {

    RESET("\u001b[0m"),
    BLUE("\u001b[1;34m"),
    RED("\u001b[1;31m"),
    ORANGE("\u001b[1;33m"),
    YELLOW("\u001b[1;33m"),
    GRAY("\u001b[1;90m");

    private final String code;

    ColorSchemes(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
