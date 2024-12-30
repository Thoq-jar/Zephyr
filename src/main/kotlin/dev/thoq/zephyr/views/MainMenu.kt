package dev.thoq.zephyr.views

import dev.thoq.zephyr.utility.misc.Io
import dev.thoq.zephyr.utility.misc.Zephyr
import dev.thoq.zephyr.utility.ui.Gui
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
    fun show(primaryStage: Stage, mainMenu: MainMenu) {
        val root = VBox()
        root.alignment = Pos.CENTER
        root.spacing = 20.0
        root.padding = Insets(20.0)

        val editor = Editor()

        val dropdownButton = Button("Convert")
        dropdownButton.style = "-fx-background-color: #333333; -fx-text-fill: #ffffff;"
        dropdownButton.padding = Insets(5.0, 10.0, 5.0, 10.0)

        val dropdownMenu = ContextMenu()
        dropdownMenu.style = "-fx-background-color: #333333;"

        val docxToHtml = MenuItem("Docx to HTML")
        docxToHtml.style = "-fx-text-fill: #ffffff;"
        docxToHtml.setOnAction {
            Gui.showAlert(
                "Notice",
                "This feature is still in development.",
                Alert.AlertType.INFORMATION
            )
        }
        dropdownMenu.items.add(docxToHtml)

        dropdownButton.setOnMouseClicked { event ->
            dropdownMenu.show(dropdownButton, event.screenX, event.screenY)
        }

        val newButton = Button("New")
        newButton.style = "-fx-background-color: #333333; -fx-text-fill: #ffffff;"
        newButton.padding = Insets(5.0, 10.0, 5.0, 10.0)
        newButton.setOnAction {
            val editorStage = Stage()
            editorStage.setOnHidden { primaryStage.show() }
            try {
                primaryStage.hide()
            } catch (ex: Exception) {
                Io.debug(ex)
            }

            editor.showEditor(editorStage)
        }

        val openButton = Button("Open")
        openButton.style = "-fx-background-color: #333333; -fx-text-fill: #ffffff;"
        openButton.padding = Insets(5.0, 10.0, 5.0, 10.0)
        openButton.setOnAction {
            val fileChooser = FileChooser()
            fileChooser.title = "Open File"
            val file = fileChooser.showOpenDialog(primaryStage)
            if (file != null) {
                val editorStage = Stage()
                editorStage.setOnHidden { primaryStage.show() }
                try {
                    primaryStage.hide()
                } catch (ex: Exception) {
                    Io.debug(ex)
                }
                editor.openFileInEditor(file, editorStage)
            }
        }

        val spacer = Region()
        spacer.prefHeight = 20.0

        root.children.addAll(dropdownButton, spacer, newButton, openButton)
        root.style = "-fx-background-color: #222222;"

        val mainMenuTitle = Zephyr.formatTitle("Main Menu")
        val scene = Scene(root, 400.0, 300.0)
        primaryStage.scene = scene
        primaryStage.title = mainMenuTitle

        primaryStage.setOnCloseRequest {
            primaryStage.close()
        }

        primaryStage.show()
    }
}