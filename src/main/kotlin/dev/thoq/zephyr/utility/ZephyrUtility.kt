package dev.thoq.zephyr.utility

import dev.thoq.zephyr.utility.misc.*
import dev.thoq.zephyr.utility.ui.*

val Zephyr = ZephyrUtility()

/**
 * A utility class that aggregates various utility components and provides helper methods
 * for the Zephyr application.
 *
 * This class includes:
 * - Access to input/output utilities via [IoUtility].
 * - GUI-related utilities via [GuiUtility].
 * - Screen-related utilities via [ScreenUtility].
 */
class ZephyrUtility {

    val io = IoUtility()
    val gui = GuiUtility()
    val screen = ScreenUtility()

    fun getName(): String = "Zephyr"
    fun formatTitle(title: String): String = "${getName()} - $title"
}