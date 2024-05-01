package Utils.Logger.Enums;

/**
 * Defines ANSI color codes for use in logging outputs. This enumeration encapsulates different
 * color codes that can be used to enhance the visibility and differentiation of log messages
 * based on their significance or category.
 *
 * Each enum constant represents a specific ANSI color code:
 * - {@code RESET} resets the color to the default terminal color.
 * - {@code BLUE} provides a bright blue color suitable for informational messages.
 * - {@code RED} is typically used for error messages, indicating critical issues.
 * - {@code ORANGE} and {@code YELLOW} are intended for warning messages.
 * - {@code GRAY} is used for debug or less significant messages.
 *
 * Usage of these color codes helps in distinguishing different types of log messages at a glance,
 * which can be particularly useful in environments where quick log analysis is required.
 */
public enum ColorSchemes {

    RESET("\u001b[0m"),  // Resets the color to the default.
    BLUE("\u001b[1;34m"), // Bright blue, often used for informational messages.
    RED("\u001b[1;31m"), // Bright red, used for errors or critical warnings.
    ORANGE("\u001b[1;33m"), // Orange, typically used for warnings.
    YELLOW("\u001b[1;33m"), // Yellow, synonymous with ORANGE, used interchangeably for warnings.
    GRAY("\u001b[1;90m"); // Gray, suitable for debug or less critical information.

    private final String code; // The ANSI escape code associated with the color.

    /**
     * Constructs a new {@code ColorSchemes} enum constant with the specified ANSI code.
     *
     * @param code the ANSI escape code that represents the color.
     */
    ColorSchemes(String code) {
        this.code = code;
    }

    /**
     * Returns the ANSI escape code of the color.
     *
     * @return the ANSI code as a string.
     */
    @Override
    public String toString() {
        return this.code;
    }
}
