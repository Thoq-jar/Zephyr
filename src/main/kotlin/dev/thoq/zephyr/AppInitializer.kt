package dev.thoq.zephyr

import com.google.gson.Gson
import com.google.gson.JsonObject
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

class AppInitializer {
    private var isDarkMode: Boolean = false
    private val configPath: Path = Paths.get(System.getProperty("user.home"), ".config", "zephyr", "config.json")
    private val gson = Gson()
    private var currentFile: File? = null

    fun showEditor(stage: Stage) {
        isDarkMode = loadThemePreference()

        val textArea = createTextArea()

        val menuBar = MenuBar()

        val fileMenu = Menu("File")
        val saveMenuItem = MenuItem("Save") // Ctrl+S
        val saveAsMenuItem = MenuItem("Save As") // Ctrl+Shift+S
        val openMenuItem = MenuItem("Open") // Ctrl+O
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

    // ---------- File Operations ----------
    private fun saveFile(textArea: TextArea, stage: Stage, file: File?) {
        if (file != null) {
            try {
                PrintWriter(file).use { writer -> writer.write(textArea.text) }
            } catch (e: Exception) {
                Io.err("Failed to save the file: ${e.message}")
                showAlert("Error", "Failed to save the file!", Alert.AlertType.ERROR)
            }
        } else {
            saveFileAs(textArea, stage)
        }
    }

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
                    }
                }
                showAlert("Error", "Failed to open the file!", Alert.AlertType.ERROR)
            }
        }
    }

    private fun showAlert(title: String, message: String, alertType: Alert.AlertType) {
        Alert(alertType).apply {
            this.title = title
            headerText = null
            contentText = message
            showAndWait()
        }
    }

    // ---------- Theme Configuration ----------
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