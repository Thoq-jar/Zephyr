package dev.thoq.zephyr.views

import dev.thoq.zephyr.utility.misc.Io
import dev.thoq.zephyr.utility.misc.Zephyr
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.scene.control.ContextMenu

class MainMenu {
    fun show(primaryStage: Stage, mainMenu: MainMenu) {
        val root = VBox()
        val editor = Editor()

        val dropdownButton = Button("Convert")
        val dropdownMenu = ContextMenu()

        val docxToHtml = javafx.scene.control.MenuItem("Docx to HTML")
        dropdownMenu.items.add(docxToHtml)

        dropdownButton.setOnMouseClicked { event ->
            dropdownMenu.show(dropdownButton, event.screenX, event.screenY)
        }

        val newButton = Button("New")
        newButton.style = "-fx-background-color: #333333; -fx-text-fill: #ffffff;"
        newButton.setOnAction {
            val editorStage = Stage()
            editorStage.setOnHidden { primaryStage.show() }
            try {
                primaryStage.hide()
            } catch (ex: Exception) {
                when (ex) {
                    is IllegalThreadStateException -> Io.debug(ex)
                }
            }

            editor.showEditor(editorStage, mainMenu)
        }

        root.children.addAll(dropdownButton, newButton)
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