package dev.thoq.zephyr.views

import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.thoq.zephyr.utility.ui.Gui
import dev.thoq.zephyr.utility.misc.Io
import dev.thoq.zephyr.utility.misc.Zephyr
import dev.thoq.zephyr.utility.ui.ZScreen
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import javafx.stage.WindowEvent
import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * Represents a text editor with functionalities such as file operations, theme management, and user interface controls.
 *
 * The `Editor` class provides methods to display the main editor window, manage themes (dark and light modes), handle file operations
 * such as saving and opening files, and interact with users through a graphical user interface. The editor also features keyboard
 * shortcuts for common actions and saves user preferences in a configuration file.
 */
class Editor {
    private val name = Zephyr.getName()
    private var isDarkMode: Boolean = false
    private val configPath: Path =
        Paths.get(System.getProperty("user.home"), ".config", name.lowercase(), "config.json")
    private val gson = Gson()
    private var currentFile: File? = null

    /**
     * Displays the text editor interface in the given stage.
     *
     * @param stage The JavaFX `Stage` where the editor will be displayed.
     * @param file Optional file to open in the editor.
     */
    fun showEditor(stage: Stage, file: File? = null) {
        isDarkMode = loadThemePreference()

        val textArea = createTextArea()
        currentFile = file

        file?.let {
            try {
                val content = it.readText()
                textArea.text = content
            } catch (ex: Exception) {
                Io.err("Failed to open the file: ${ex.message}")
                Gui.showAlert("Error", "Failed to open the file!", Alert.AlertType.ERROR)
                return
            }
        }

        val scene = createEditorScene(stage, textArea)

        val editorTitle = if (file != null) {
            Zephyr.formatTitle("Editor - ${file.name}")
        } else {
            Zephyr.formatTitle("Editor")
        }

        stage.title = editorTitle
        stage.scene = scene

        stage.setOnCloseRequest {
            closeEditor(stage, it)
        }

        stage.showingProperty().addListener { _, _, newValue ->
            if (newValue) adjustStagePosition(stage)
        }

        stage.show()
        Io.println("$editorTitle opened!")
    }

    /**
     * Creates the editor scene with the given TextArea and attaches menu actions.
     *
     * @param stage The JavaFX `Stage` where the editor will be displayed.
     * @param textArea The TextArea to be used in the editor.
     * @return The created Scene object.
     */
    private fun createEditorScene(stage: Stage, textArea: TextArea): Scene {
        val menuBar = MenuBar()

        val fileMenu = Menu("File")
        val saveMenuItem = MenuItem("Save")
        val saveAsMenuItem = MenuItem("Save As")
        val openMenuItem = MenuItem("Open")
        val closeMenuItem = MenuItem("Close")
        fileMenu.items.addAll(openMenuItem, saveMenuItem, saveAsMenuItem, closeMenuItem)

        saveMenuItem.setOnAction {
            saveFile(textArea, stage, currentFile)
        }

        saveAsMenuItem.setOnAction {
            saveFileAs(textArea, stage)
        }

        openMenuItem.setOnAction {
            openFile(textArea, stage)
        }

        closeMenuItem.setOnAction {
            stage.fireEvent(WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST))
        }

        val viewMenu = Menu("View")
        val toggleThemeMenuItem = MenuItem("Toggle Theme")
        viewMenu.items.add(toggleThemeMenuItem)
        toggleThemeMenuItem.setOnAction {
            isDarkMode = !isDarkMode
            saveThemePreference(isDarkMode)
            applyTheme(stage.scene, isDarkMode)
        }

        menuBar.menus.addAll(fileMenu, viewMenu)

        val scene = Scene(BorderPane().apply {
            top = menuBar
            center = textArea
        }, 1230.0, 760.0)

        scene.accelerators[KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)] = Runnable {
            saveFile(textArea, stage, currentFile)
        }
        scene.accelerators[KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)] =
            Runnable {
                saveFileAs(textArea, stage)
            }
        scene.accelerators[KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)] = Runnable {
            openFile(textArea, stage)
        }

        applyTheme(scene, isDarkMode)

        return scene
    }

    /**
     * Opens a file in the editor by delegating to `showEditor`.
     *
     * @param file The file to be opened.
     * @param stage The stage where the editor will be displayed.
     */
    fun openFileInEditor(file: File, stage: Stage) {
        showEditor(stage, file)
    }

    /**
     * Adjusts the position of the specified stage to the center of the primary screen's visual bounds.
     *
     * This function calculates the center position of the primary screen's visual area and moves the given stage
     * to this position. It ensures that the stage is properly aligned and centered within the available screen space,
     * taking into account the dimensions of the stage and the visual bounds.
     *
     * @param stage The JavaFX `Stage` to be repositioned.
     */
    private fun adjustStagePosition(stage: Stage) {
        val screenBounds = ZScreen.getCurrentMouseScreenBounds()

        stage.x = screenBounds.minX + (screenBounds.width - stage.width) / 2
        stage.y = screenBounds.minY + (screenBounds.height - stage.height) / 2
    }

    /**
     * Closes the text editor stage and consumes the corresponding window close event.
     *
     * This method is invoked when the user attempts to close the editor window. It handles
     * the close request by consuming the provided `WindowEvent` to prevent additional
     * event propagation and subsequently closes the specified JavaFX `Stage`.
     *
     * @param stage The JavaFX `Stage` representing the editor window to be closed.
     * @param it The `WindowEvent` associated with the close request, which will be consumed.
     */
    private fun closeEditor(stage: Stage, it: WindowEvent) {
        it.consume()
        stage.close()
    }

    /**
     * Applies the specified theme to the provided scene. This method updates the background color,
     * text color, and other relevant UI properties based on the selected theme (dark mode or light mode).
     *
     * @param scene The Scene to which the theme will be applied.
     * @param darkMode A Boolean indicating whether the dark mode should be applied (true)
     * or light mode should be applied (false).
     */
    private fun applyTheme(scene: Scene, darkMode: Boolean) {
        val root = scene.root as BorderPane
        val textArea = root.center as TextArea
        val menuBar = root.top as MenuBar

        when (darkMode) {
            true -> {
                scene.fill = Color.web("#2e2e2e")
                root.style = "-fx-background-color: #2e2e2e;"
                textArea.style = """
                    -fx-background-color: #1e1e1e;
                    -fx-text-fill: #ffffff;
                    -fx-prompt-text-fill: #808080;
                    -fx-control-inner-background: #1e1e1e;
                """.trimIndent()
            }

            false -> {
                scene.fill = Color.web("#ffffff")
                root.style = "-fx-background-color: #ffffff;"
                textArea.style = """
                    -fx-background-color: #f5f5f5;
                    -fx-text-fill: #000000;
                    -fx-prompt-text-fill: #a9a9a9;
                    -fx-control-inner-background: #f5f5f5;
                """.trimIndent()
            }
        }

        menuBar.style = when (darkMode) {
            true -> "-fx-background-color: #444444; -fx-text-fill: white;"
            false -> "-fx-background-color: #e6e6e6; -fx-text-fill: black;"
        }

        menuBar.menus.forEach { menu ->
            menu.style = when (darkMode) {
                true -> "-fx-text-fill: white;"
                false -> "-fx-text-fill: black;"
            }
            menu.items.forEach { item ->
                item.style = when (darkMode) {
                    true -> "-fx-text-fill: white; -fx-background-color: #444444;"
                    false -> "-fx-text-fill: black;"
                }
            }
        }
    }

    /**
     * Creates and configures a TextArea with a default prompt text and wraps text content.
     * The TextArea's prompt text is cleared when the user starts typing.
     *
     * @return A configured TextArea instance with text wrapping enabled and a default prompt text.
     */
    private fun createTextArea(): TextArea {
        val textArea = TextArea()
        textArea.textProperty().addListener { _, oldValue, newValue ->
            if (oldValue.isEmpty() && newValue.isNotEmpty()) textArea.promptText = null
        }
        textArea.promptText = "Type here, begin typing to dismiss..."
        textArea.isWrapText = true
        return textArea
    }

    /**
     * Saves the content of the provided TextArea to the specified file. If the file is null,
     * it triggers the saveFileAs function to prompt the user to pick a file before saving.
     *
     * @param textArea The TextArea containing the text to be saved.
     * @param stage The current Stage, used if a file needs to be selected.
     * @param file The target File where the text should be saved. If null, the function will invoke saveFileAs.
     */
    private fun saveFile(textArea: TextArea, stage: Stage, file: File?) {
        when (file) {
            null -> saveFileAs(textArea, stage)
            else -> try {
                PrintWriter(file).use { writer -> writer.write(textArea.text) }
            } catch (ex: Exception) {
                Io.err("Failed to save the file: ${ex.message}")
                Gui.showAlert("Error", "Failed to save the file!", Alert.AlertType.ERROR)
            }
        }
    }

    /**
     * Saves the content of the given TextArea to a file after prompting the user to select a file location.
     * The function opens a file save dialog allowing the user to specify the name and location
     * of the file to be saved.
     *
     * @param textArea The TextArea containing the content to be saved.
     * @param stage The current Stage, used as the owner for the file save dialog.
     */
    private fun saveFileAs(textArea: TextArea, stage: Stage) {
        val fileChooser = FileChooser()
        fileChooser.title = "Save As"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("Text Files", "*.txt"),
            FileChooser.ExtensionFilter("Markdown Files", "*.md"),
            FileChooser.ExtensionFilter("Configuration Files", "*.json", "*.yaml", "*.yml", "*.*")
        )
        val file = fileChooser.showSaveDialog(stage) ?: return
        currentFile = file
        saveFile(textArea, stage, file)
    }

    /**
     * Opens a file dialog to allow the user to select a text file to open. The contents of the selected file
     * are then read and displayed in the provided TextArea. Handles various exceptions that may occur during
     * file reading and displays appropriate error messages.
     *
     * @param textArea The TextArea where the content of the opened file will be displayed.
     * @param stage The current Stage, used as the owner for the file dialog.
     */
    private fun openFile(textArea: TextArea, stage: Stage) {
        val fileChooser = FileChooser()
        fileChooser.title = "Open File"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Text Files", "*.txt"))
        val file = fileChooser.showOpenDialog(stage) ?: return
        try {
            val content = file.readText()
            textArea.text = content
            currentFile = file
        } catch (ex: Exception) {
            Io.err("Failed to open the file: ${ex.message}")
            Io.err("Cause: ${ex.cause}")
            Io.println("More simple explanation:")
            val message = when (ex.cause) {
                is java.nio.file.NoSuchFileException -> "This means the file does not exist."
                is java.nio.file.AccessDeniedException -> "The file is locked by another process or you don't have permission to access it."
                is java.io.IOException -> "An I/O error occurred while trying to access the file."
                is SecurityException -> "Access to the file was denied due to security restrictions."
                is NoSuchElementException -> "The file is empty or does not contain valid content."
                is java.nio.file.InvalidPathException -> "The file path is invalid, cannot be accessed."
                null -> "The file has no contents (Is the file empty?)"
                else -> {
                    Io.println("The cause is not clear. Please check the error message for more details.")
                    Io.println("Stack trace:")
                    Io.println("---------------- Start stack trace ----------------")
                    ex.printStackTrace()
                    Io.println("---------------- End of stack trace ----------------")
                    Io.println("The exception class is: ${ex.javaClass.name}")
                    Io.println("End of stack trace.")
                    Io.github("https://github.com/thoq-jar/zephyr")
                    "I really appreciate it as *any* feedback helps the project!"
                }
            }
            Io.println(message)
            Gui.showAlert("Error", "Failed to open the file!", Alert.AlertType.ERROR)
        }
    }

    /**
     * Loads the user's theme preference from a configuration file.
     * Attempts to read the "theme" property from the configuration file content.
     * If the value of the "theme" property is "dark", returns true; otherwise, returns false.
     * If the file does not exist or an error occurs during reading, defaults to false.
     *
     * @return true if the theme preference is set to "dark", otherwise false.
     */
    private fun loadThemePreference(): Boolean {
        return try {
            when {
                Files.exists(configPath) -> {
                    val content = Files.readString(configPath)
                    val json = gson.fromJson(content, JsonObject::class.java)
                    json.get("theme")?.asString == "dark"
                }

                else -> false
            }
        } catch (ex: Exception) {
            Io.err("Failed to load theme preference: ${ex.message}")
            false
        }
    }

    /**
     * Saves the user's theme preference to a configuration file.
     * The preference is stored as a JSON object in the file, with the theme
     * property set to "dark" for dark mode or "light" for light mode.
     *
     * If the configuration directory does not exist, it will be created.
     * In case of an error during the save operation, an error message
     * will be logged.
     *
     * @param isDarkMode A boolean indicating whether the dark mode is enabled (true)
     * or light mode is enabled (false).
     */
    private fun saveThemePreference(isDarkMode: Boolean) {
        try {
            val parentDir = configPath.parent
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir)
            }

            val json = JsonObject()
            json.addProperty("theme", if (isDarkMode) "dark" else "light")
            Files.writeString(configPath, gson.toJson(json))
        } catch (ex: Exception) {
            Io.err("Failed to save theme preference: ${ex.message}")
        }
    }
}