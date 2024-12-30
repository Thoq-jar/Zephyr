package dev.thoq.zephyr

import com.google.gson.Gson
import com.google.gson.JsonObject
import dev.thoq.zephyr.utility.Gui
import dev.thoq.zephyr.utility.Io
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * AppInitializer is responsible for initializing and managing the main application interface.
 * It provides functionality to display an editor window, manage theme settings, handle file
 * operations (e.g., save, open), and apply UI styles based on user preferences.
 */
class AppInitializer {
    private var isDarkMode: Boolean = false
    private val configPath: Path = Paths.get(System.getProperty("user.home"), ".config", "zephyr", "config.json")
    private val gson = Gson()
    private var currentFile: File? = null

    /**
     * Displays the main editor window with text editing capabilities, menu bars for file and view operations,
     * and shortcut accelerators for quick access to operations.
     *
     * @param stage The primary stage where the editor UI is displayed.
     */
    fun showEditor(stage: Stage) {
        isDarkMode = loadThemePreference()

        val textArea = createTextArea()

        val menuBar = MenuBar()

        val fileMenu = Menu("File")
        val saveMenuItem = MenuItem("Save")
        val saveAsMenuItem = MenuItem("Save As")
        val openMenuItem = MenuItem("Open")
        fileMenu.items.addAll(openMenuItem, saveMenuItem, saveAsMenuItem)

        saveMenuItem.setOnAction {
            saveFile(textArea, stage, currentFile)
        }

        saveAsMenuItem.setOnAction {
            saveFileAs(textArea, stage)
        }

        openMenuItem.setOnAction {
            openFile(textArea, stage)
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
        }, 1230.0, 850.0)

        scene.accelerators[KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN)] = Runnable {
            saveFile(textArea, stage, currentFile)
        }
        scene.accelerators[KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)] = Runnable {
            saveFileAs(textArea, stage)
        }
        scene.accelerators[KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN)] = Runnable {
            openFile(textArea, stage)
        }

        applyTheme(scene, isDarkMode)

        stage.title = "Zephyr"
        stage.scene = scene
        stage.show()

        Io.println("Done!")
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

        if (darkMode) {
            scene.fill = Color.web("#2e2e2e")
            root.style = "-fx-background-color: #2e2e2e;"
            textArea.style = """
                -fx-background-color: #1e1e1e;
                -fx-text-fill: #ffffff;
                -fx-prompt-text-fill: #808080;
                -fx-control-inner-background: #1e1e1e;
            """.trimIndent()
        } else {
            scene.fill = Color.web("#ffffff")
            root.style = "-fx-background-color: #ffffff;"
            textArea.style = """
                -fx-background-color: #f5f5f5;
                -fx-text-fill: #000000;
                -fx-prompt-text-fill: #a9a9a9;
                -fx-control-inner-background: #f5f5f5;
            """.trimIndent()
        }

        menuBar.style = if (darkMode) {
            "-fx-background-color: #444444; -fx-text-fill: white;"
        } else {
            "-fx-background-color: #e6e6e6; -fx-text-fill: black;"
        }

        menuBar.menus.forEach { menu ->
            menu.style = if (darkMode) "-fx-text-fill: white;" else "-fx-text-fill: black;"
            menu.items.forEach { item ->
                item.style = if (darkMode) "-fx-text-fill: white; -fx-background-color: #444444;" else "-fx-text-fill: black;"
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
            if (oldValue.isEmpty() && newValue.isNotEmpty()) {
                textArea.promptText = null
            }
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
        if (file != null) {
            try {
                PrintWriter(file).use { writer -> writer.write(textArea.text) }
            } catch (e: Exception) {
                Io.err("Failed to save the file: ${e.message}")
                Gui.showAlert("Error", "Failed to save the file!", Alert.AlertType.ERROR)
            }
        } else {
            saveFileAs(textArea, stage)
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
        val file = fileChooser.showOpenDialog(stage)
        if (file != null) {
            try {
                val content = Scanner(file).useDelimiter("\\Z").next()
                textArea.text = content
                currentFile = file
            } catch (ex: Exception) {
                Io.err("Failed to open the file: ${ex.message}")
                Io.err("Cause: ${ex.cause}")
                Io.println("More simple explanation:")
                when (ex.cause) {
                    is java.nio.file.NoSuchFileException -> Io.println("This means the file does not exist.")
                    is java.nio.file.AccessDeniedException ->
                        Io.println("The file is locked by another process or you don't have permission to access it.")
                    is java.io.IOException -> Io.println("An I/O error occurred while trying to access the file.")
                    is SecurityException -> Io.println("Access to the file was denied due to security restrictions.")
                    is NoSuchElementException -> Io.println("The file is empty or does not contain valid content.")
                    is java.nio.file.InvalidPathException -> Io.println("The file path is invalid, cannot be accessed.")
                    null -> Io.println("The file has no contents (Is the file empty?)")
                    else -> {
                        Io.println("The cause is not clear. Please check the error message for more details.")
                        Io.println("Stack trace:")
                        Io.println("---------------- Start stack trace ----------------")
                        ex.printStackTrace()
                        Io.println("---------------- End of stack trace ----------------")
                        Io.println("The exception class is: ${ex.javaClass.name}")
                        Io.println("End of stack trace.")
                        Io.println("If you are unsatisfied, you may open an issue on GitHub or make a pull request.")
                        Io.github("https://github.com/thoq-jar/zephyr")
                        Io.println("I really appricieate it as *any* feedback helps the project!")
                    }
                }
                Gui.showAlert("Error", "Failed to open the file!", Alert.AlertType.ERROR)
            }
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
            if (Files.exists(configPath)) {
                val content = Files.readString(configPath)
                val json = gson.fromJson(content, JsonObject::class.java)
                json.get("theme")?.asString == "dark"
            } else {
                false
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