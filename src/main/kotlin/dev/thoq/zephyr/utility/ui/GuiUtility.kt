package dev.thoq.zephyr.utility.ui

import dev.thoq.zephyr.utility.misc.Zephyr
import javafx.scene.control.Alert

val Gui = GuiUtility()

/**
 * Provides utility methods for graphical user interface interactions.
 */
class GuiUtility {
    val name = Zephyr.getName()
    /**
     * Displays an alert dialog with the specified title, message, and alert type.
     *
     * @param title The title of the alert dialog.
     * @param message The message content displayed within the alert dialog.
     * @param alertType The type of alert (e.g., ERROR, WARNING, INFORMATION, etc.) to be shown.
     */
    fun showAlert(title: String, message: String, alertType: Alert.AlertType) {
        Alert(alertType).apply {
            this.title = "$name - $message"
            headerText = null
            contentText = message
            showAndWait()
        }
    }
}