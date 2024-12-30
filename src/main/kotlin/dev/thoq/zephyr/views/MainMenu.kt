package dev.thoq.zephyr.views

import dev.thoq.zephyr.utility.Zephyr
import javafx.geometry.Pos
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.layout.VBox
import javafx.scene.layout.Region
import javafx.stage.FileChooser
import javafx.stage.Stage

class MainMenu {
    private val io = Zephyr.io
    private val gui = Zephyr.gui
    /**
     * Displays the main menu of the application on the provided stage.
     *
     * The menu consists of the following components:
     * - A "Convert" button that opens a dropdown menu. Currently, the "Docx to HTML"
     *   feature in the dropdown menu is under development and displays a notice.
     * - A "New" button that launches a new editor stage while hiding the main menu.
     * - An "Open" button that allows the user to select a file to open in the editor.
     *
     * The layout is styled using a vertical box (`VBox`) aligned at the center with
     * spacing and padding for its components. The primary stage is configured with
     * close request handling and a formatted title. The method also logs when the
     * setup is completed.
     *
     * @param primaryStage The primary application stage on which the main menu is displayed.
     */
    fun show(primaryStage: Stage) {
        val root = VBox().apply {
            alignment = Pos.CENTER
            spacing = 20.0
            padding = Insets(20.0)
        }

        val editor = Editor()

        val dropdownButton = Button("Convert").apply {
            style = "-fx-background-color: #333333; -fx-text-fill: #ffffff;"
            padding = Insets(5.0, 10.0, 5.0, 10.0)
        }

        val dropdownMenu = ContextMenu().apply {
            style = "-fx-background-color: #333333;"
        }

        val docxToHtml = MenuItem("Docx to HTML").apply {
            style = "-fx-text-fill: #ffffff;"
            setOnAction {
                gui.showAlert(
                    "Notice",
                    "This feature is still in development.",
                    Alert.AlertType.INFORMATION
                )
            }
        }
        dropdownMenu.items.add(docxToHtml)

        dropdownButton.setOnMouseClicked { event ->
            dropdownMenu.show(dropdownButton, event.screenX, event.screenY)
        }

        val newButton = createButton("New") {
            val editorStage = Stage()
            editorStage.setOnHidden { primaryStage.show() }
            handleStage(primaryStage) {
                editor.showEditor(editorStage)
            }
        }

        val openButton = createButton("Open") {
            val fileChooser = FileChooser().apply { title = "Open File" }
            val file = fileChooser.showOpenDialog(primaryStage)
            if (file != null) {
                val editorStage = Stage()
                editorStage.setOnHidden { primaryStage.show() }
                handleStage(primaryStage) {
                    editor.openFileInEditor(file, editorStage)
                }
            }
        }

        val spacer = Region().apply { prefHeight = 20.0 }

        root.children.addAll(dropdownButton, spacer, newButton, openButton)
        root.style = "-fx-background-color: #111111;"

        val mainMenuTitle = Zephyr.formatTitle("Main Menu")
        primaryStage.scene = Scene(root, 400.0, 300.0).apply {
            primaryStage.title = mainMenuTitle
            primaryStage.setOnCloseRequest { primaryStage.close() }
        }
        primaryStage.show()
        io.println("Done!")
    }

    /**
     * Creates a styled Button with the specified text and click action.
     *
     * The button is styled with a custom dark background and white text, along with padding.
     * The provided lambda function is triggered when the button is clicked.
     *
     * @param text The text to display on the button.
     * @param onClick A lambda function to execute when the button is clicked.
     * @return A Button instance with the applied styling and click behavior.
     */
    private fun createButton(text: String, onClick: () -> Unit): Button {
        return Button(text).apply {
            style = "-fx-background-color: #333333; -fx-text-fill: #ffffff;"
            padding = Insets(5.0, 10.0, 5.0, 10.0)
            setOnAction { onClick() }
        }
    }

    /**
     * Handles the transition from the primary stage to another operation or scene.
     *
     * This method hides the primary stage and executes the given action. It also logs messages
     * before and after hiding the primary stage. If an exception occurs during execution, it is
     * logged using the debugging utility.
     *
     * @param primaryStage The primary application stage that is being managed.
     * @param action A lambda function to execute after the primary stage is hidden.
     */
    private fun handleStage(primaryStage: Stage, action: () -> Unit) {
        try {
            io.println("Hiding main menu...")
            primaryStage.hide()
            io.println("Finished: hiding main menu!")
            action()
        } catch (ex: Exception) {
            io.debug(ex)
        }
    }
}