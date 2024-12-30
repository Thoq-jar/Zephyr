package dev.thoq.zephyr.utility.misc

val Zephyr = ZephyrUtility()

class ZephyrUtility {
    fun getName(): String = "Zephyr"
    fun formatTitle(title: String): String = "${getName()} - $title"
}