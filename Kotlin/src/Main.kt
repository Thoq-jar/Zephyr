@file:Suppress("DEPRECATION")

import java.awt.*
import java.awt.Color.WHITE
import java.awt.event.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.KeyStroke

object SwingTextEditor {
    private const val ZephyrLIGHT_THEME = "Zephyr Light"
    private const val Zephyr_THEME = "Zephyr Dark"
    private const val DARK_PURPLE_THEME: String = "Dark Purple"
    private const val OLED_THEME = "OLED"
    private const val SPLASH_FILE_NAME = "splash.png"
    private const val SPLASH_DURATION_MS = 3000

    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater {
            val splashURL = SwingTextEditor::class.java.getResource(SPLASH_FILE_NAME)

            if (splashURL == null) {
                println("Splash image file not found: $SPLASH_FILE_NAME")
                return@invokeLater
            }

            val splashIcon = ImageIcon(splashURL)
            val loadingFrame = JFrame("Zephyr Initalization")
            loadingFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            loadingFrame.isUndecorated = true
            loadingFrame.background = Color(0, 0, 0, 0) // Transparent background
            loadingFrame.add(JLabel(splashIcon), BorderLayout.CENTER)
            loadingFrame.pack()
            loadingFrame.setLocationRelativeTo(null)
            loadingFrame.iconImage = splashIcon.image // Set icon for the taskbar
            loadingFrame.isVisible = true

            // Add keyboard shortcuts
            loadingFrame.addKeyListener(object : KeyAdapter() {
                override fun keyPressed(e: KeyEvent) {
                    if (e.modifiers == KeyEvent.CTRL_MASK && e.keyCode == KeyEvent.VK_S) {
                        // Handle Ctrl+S (Save) shortcut
                        println("Ctrl+S pressed")
                    } else if (e.modifiers == KeyEvent.CTRL_MASK && e.keyCode == KeyEvent.VK_O) {
                        // Handle Ctrl+O (Open) shortcut
                        println("Ctrl+O pressed")
                    }
                }
            })

            // Add mouse listeners to make the frame draggable
            var mouseX = 0
            var mouseY = 0
            loadingFrame.addMouseListener(object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    mouseX = e.xOnScreen - loadingFrame.x
                    mouseY = e.yOnScreen - loadingFrame.y
                }
            })
            loadingFrame.addMouseMotionListener(object : MouseMotionAdapter() {
                override fun mouseDragged(e: MouseEvent) {
                    val newX = e.xOnScreen - mouseX
                    val newY = e.yOnScreen - mouseY
                    loadingFrame.setLocation(newX, newY)
                }
            })

            // Simulate a long loading process
            Timer(SPLASH_DURATION_MS) {
                loadingFrame.isVisible = false
                showEditor()
            }.apply {
                isRepeats = false
                start()
            }
        }
    }

    // Function to show the editor
    private fun showEditor() {
        val frame = JFrame("Zephyr Ultimate 2.29.24")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.preferredSize = Dimension(1600, 900)
        frame.contentPane.background = WHITE

        // Set Arial font
        val arialFont = Font("Arial", Font.PLAIN, 18)

        val textArea = JTextArea()
        textArea.font = arialFont
        textArea.foreground = WHITE
        textArea.background = Color(40, 42, 54)
        val scrollPane = JScrollPane(textArea)
        frame.add(scrollPane, BorderLayout.CENTER)

        val menuBar = createMenuBar(frame, textArea)
        menuBar.background = WHITE
        frame.jMenuBar = menuBar

        // Load and set application icon
        val iconURL = SwingTextEditor::class.java.getResource("icon.png")
        if (iconURL != null) {
            val icon = ImageIcon(iconURL)
            frame.iconImage = icon.image
            // Set icon for the taskbar
            frame.iconImage = icon.image
        }

        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        loadFromFile(textArea)
    }

    private fun createMenuBar(frame: JFrame, textArea: JTextArea): JMenuBar {
        val menuBar = JMenuBar()
        menuBar.background = WHITE

        val fileMenu = JMenu("File")
        val openItem = JMenuItem("Open")
        openItem.addActionListener {
            val fileChooser = JFileChooser()
            val filter = FileNameExtensionFilter("Text Files", "txt")
            fileChooser.fileFilter = filter
            val result = fileChooser.showOpenDialog(frame)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedFile = fileChooser.selectedFile
                try {
                    val content = String(Files.readAllBytes(selectedFile.toPath()))
                    textArea.text = content
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
        val saveItem = JMenuItem("Save")
        saveItem.addActionListener { saveToFile(textArea.text) }
        // Add Ctrl+S shortcut for Save
        saveItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)

        // Add Ctrl+O shortcut for Open
        openItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)
        fileMenu.add(openItem)
        fileMenu.add(saveItem)
        val themeMenu = JMenu("Theme")
        val themeGroup = ButtonGroup()
        val ZephyrlightThemeItem = JRadioButtonMenuItem(ZephyrLIGHT_THEME)
        true.also { it.also { ZephyrlightThemeItem.isSelected = it } }
        val ZephyrThemeItem = JRadioButtonMenuItem(Zephyr_THEME)
        val darkPurpleThemeItem = JRadioButtonMenuItem(DARK_PURPLE_THEME)
        val OLEDTheme = JRadioButtonMenuItem(OLED_THEME)
        themeGroup.add(ZephyrlightThemeItem)
        themeGroup.add(ZephyrThemeItem)
        themeGroup.add(darkPurpleThemeItem)
        ZephyrlightThemeItem.addActionListener {
            setTheme(frame, UIManager.getCrossPlatformLookAndFeelClassName())
            updateThemeColors(textArea, WHITE, Color.BLACK)
        }
        ZephyrThemeItem.addActionListener {
            setTheme(frame, UIManager.getSystemLookAndFeelClassName())
            updateThemeColors(textArea, Color(255, 255, 255), Color(40, 42, 54))
        }
        OLEDTheme.addActionListener {
            setTheme(frame, UIManager.getSystemLookAndFeelClassName())
            updateThemeColors(textArea, Color(0, 0, 0), WHITE)
        }
        darkPurpleThemeItem.addActionListener {
            setTheme(frame, UIManager.getSystemLookAndFeelClassName())
            updateThemeColors(textArea, Color(59, 44, 80), WHITE)
        }
        themeMenu.add(ZephyrlightThemeItem)
        themeMenu.add(ZephyrThemeItem)
        themeMenu.add(darkPurpleThemeItem)
        themeMenu.add(OLEDTheme)

        menuBar.add(fileMenu)
        menuBar.add(themeMenu)

        return menuBar
    }

    private fun setTheme(frame: JFrame, themeClassName: String) {
        try {
            UIManager.setLookAndFeel(themeClassName)
            SwingUtilities.updateComponentTreeUI(frame)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateThemeColors(textArea: JTextArea, backgroundColor: Color, foregroundColor: Color) {
        textArea.background = backgroundColor
        textArea.foreground = foregroundColor
    }

    private fun saveToFile(text: String) {
        val fileChooser = JFileChooser()
        val result = fileChooser.showSaveDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            val selectedFile = fileChooser.selectedFile
            try {
                FileWriter(selectedFile).use { writer ->
                    writer.write(text)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun loadFromFile(textArea: JTextArea) {
        val file = File("editor.txt")
        if (file.exists()) {
            try {
                val content = String(Files.readAllBytes(Paths.get("editor.txt")))
                textArea.text = content
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
