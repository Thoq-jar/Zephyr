package dev.thoq.zephyr.utility.misc

import java.lang.Exception
import kotlin.io.println as kPrintln

val Io = IoUtility();

/**
 * A utility class for handling console output with colorized and formatted messages.
 * Provides methods for printing various types of messages, such as informational messages, warnings, errors, and GitHub-specific logs.
 */
class IoUtility {
    /**
     * Applies a specific color to the provided text using ANSI escape codes. If the specified color
     * is not recognized, the text is returned unchanged.
     *
     * @param text The text to be colorized.
     * @param color The name of the color to apply. Supported colors include:
     * "red", "green", "blue", "yellow", "cyan", "magenta", "pink", "gray", "black", "white", and "reset".
     * @return The text wrapped with the ANSI escape code for the specified color, followed by a reset code.
     * If the specified color is not supported, the original text is returned without modification.
     */
    private fun colorize(text: String, color: String): String {
        val colors = mapOf(
            "red" to "\u001B[31m",
            "green" to "\u001B[32m",
            "blue" to "\u001B[34m",
            "yellow" to "\u001B[33m",
            "cyan" to "\u001B[36m",
            "magenta" to "\u001B[35m",
            "pink" to "\u001B[95m",
            "gray" to "\u001B[90m",
            "grey" to "\u001B[90m",
            "black" to "\u001B[30m",
            "white" to "\u001B[37m",
            "reset" to "\u001B[0m"
        )
        return colors[color]?.let { "$it$text${colors["reset"]}" } ?: text
    }

    /**
     * Generates a colorized prefix string for console output, formatted with brackets and a specific kind label.
     *
     * @param kind The label or type of the message to include in the prefix.
     * @param color The color to apply to the kind label using ANSI escape codes.
     * @return A formatted string with a colorized prefix, which includes brackets and the kind label.
     */
    private fun getPrefix(kind: String, color: String): String {
        return colorize("[ ", "gray") + colorize(kind, color) + colorize(" ] ", "gray")
    }

    /**
     * Prints the provided message to the console with an "INFO" prefix formatted in blue color.
     *
     * @param message The message to be printed to the console.
     */
    fun println(message: String) = kPrintln(getPrefix("INFO", "blue") + message)

    /**
     * Prints the provided message to the console with a "GITHUB" prefix formatted in cyan color.
     *
     * @param message The message to be printed to the console.
     */
    fun github(message: String) = kPrintln(getPrefix("GITHUB", "cyan") + message)

    /**
     * Prints an error message to the console with an "ERROR" prefix formatted in red color.
     *
     * @param message The error message to be printed to the console.
     */
    fun err(message: String) = kPrintln(getPrefix("ERROR", "red") + message)

    /**
     * Logs a warning message to the console with a "WARN" prefix formatted in yellow color.
     *
     * @param message The warning message to be printed to the console.
     */
    fun warn(message: String) = kPrintln(getPrefix("WARN", "yellow") + message)

    fun debug(exception: Exception) {
        kPrintln(getPrefix("DEBUG", "magenta") +
                 "A(n) ${exception.cause} has occurred! Zephyr was able to recover iself. Heres some more info:")
        kPrintln(getPrefix("DEBUG", "magenta") + "Reported cause: ${exception.cause}")
        kPrintln(getPrefix("DEBUG", "magenta") + "Stacktrace: ${exception.stackTraceToString()}")
    }
}