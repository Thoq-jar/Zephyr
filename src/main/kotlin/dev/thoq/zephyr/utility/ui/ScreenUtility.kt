package dev.thoq.zephyr.utility.ui

import java.awt.MouseInfo
import javafx.stage.Screen as JavaFxScreen
val ZScreen = ScreenUtility()

/**
 * A utility class for retrieving information about the screen where the mouse pointer is currently located.
 */
class ScreenUtility {

    /**
     * Retrieves the bounds of the screen where the mouse pointer is currently located.
     *
     * @return A `Rectangle2D` object representing the boundaries of the screen containing the current mouse pointer.
     * If the screen cannot be determined, the bounds of the primary screen are returned.
     */
    fun getCurrentMouseScreenBounds(): javafx.geometry.Rectangle2D {
        val mouseLocation = MouseInfo.getPointerInfo().location
        val screen = JavaFxScreen.getScreensForRectangle(
            mouseLocation.x.toDouble(),
            mouseLocation.y.toDouble(),
            1.0,
            1.0
        ).firstOrNull() ?: JavaFxScreen.getPrimary()
        return screen.bounds
    }
}