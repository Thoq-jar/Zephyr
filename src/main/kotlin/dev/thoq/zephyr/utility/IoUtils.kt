package dev.thoq.zephyr.utility

import kotlin.io.println as kPrintln

@Suppress("SameParameterValue")
val Io = IoUtils();

class IoUtils {
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

    private fun getPrefix(kind: String, color: String): String {
        return colorize("[ ", "gray") + colorize(kind, color) + colorize(" ] ", "gray")
    }

    fun println(message: String) = kPrintln(getPrefix("INFO", "blue") + message)
    fun github(message: String) = kPrintln(getPrefix("GITHUB", "cyan") + message)
    fun err(message: String) = kPrintln(getPrefix("ERROR", "red") + message)
    fun warn(message: String) = kPrintln(getPrefix("WARN", "yellow") + message)
}