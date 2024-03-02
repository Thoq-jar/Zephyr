import java.awt.*
import java.awt.Color.WHITE
import java.awt.event.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.*
import javax.swing.border.Border
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

object SwingTextEditor {
    private const val ZephyrLIGHT_THEME = "Zephyr Light"
    private const val Zephyr_THEME = "Zephyr Dark"
    private const val DARK_PURPLE_THEME: String = "Dark Purple"
    private const val OLED_THEME = "OLED"
    private const val SPLASH_FILE_NAME = "splash.png"
    private const val ICON_FILE_NAME = "icon.png"
    private const val SPLASH_DURATION_MS = 4000

    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater {
            val splashURL = SwingTextEditor::class.java.getResource(SPLASH_FILE_NAME)

            if (splashURL == null) {
                println("Splash image file not found: $SPLASH_FILE_NAME")
                return@invokeLater
            }

            val splashIcon = ImageIcon(splashURL)
            val loadingFrame = JFrame("Zephyr 2.29.24")
            loadingFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            loadingFrame.isUndecorated = true
            loadingFrame.background = Color(0, 0, 0, 0)
            loadingFrame.add(JLabel(splashIcon), BorderLayout.CENTER)
            loadingFrame.pack()
            loadingFrame.setLocationRelativeTo(null)

            val iconURL = SwingTextEditor::class.java.getResource("icon.png")
            if (iconURL != null) {
                val icon = ImageIcon(iconURL)
                loadingFrame.iconImage = icon.image
                loadingFrame.iconImage = icon.image
            } else {
                println("Naw icon dont exist fam")
            }

            loadingFrame.isVisible = true

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

            Timer(SPLASH_DURATION_MS) {
                loadingFrame.isVisible = false
                welcome()
            }.apply {
                isRepeats = false
                start()
            }
        }
    }

    private fun welcome() {
        val darkTheme = mapOf(
            "Panel.background" to Color(20, 20, 20),
            "Button.background" to Color(56, 58, 70),
            "Button.border" to BorderFactory.createEmptyBorder(10, 20, 10, 20)
        )
        for ((key, value) in darkTheme) {
            UIManager.put(key, value)
        }

        val icon = ImageIcon("icon.png")
        val iconLabel = JLabel(icon)

        val logoPanel = JPanel(BorderLayout()).apply {
            background = Color(20, 20, 20)
            add(iconLabel)
        }

        val welcomeDialog = JDialog()
        welcomeDialog.title = "Zephyr Ultimate 1.3.24"
        welcomeDialog.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        welcomeDialog.isResizable = true
        welcomeDialog.setSize(1600, 900)
        welcomeDialog.setLocationRelativeTo(null)
        welcomeDialog.layout = BorderLayout()

        welcomeDialog.add(logoPanel, BorderLayout.NORTH)

        val welcomeLabel = JLabel("Welcome to Zephyr Ultimate!")
        welcomeLabel.horizontalAlignment = SwingConstants.CENTER
        welcomeLabel.font = Font("Arial", Font.BOLD, 100)
        welcomeDialog.add(welcomeLabel, BorderLayout.CENTER)

        val buttonPanel = JPanel(GridBagLayout()).apply {
            background = Color(20, 20, 20)
        }

        val createFileButton = JButton("New Project").apply {
            background = Color(100, 100, 100)
            foreground = WHITE
            border = RoundedBorder(10)
            addActionListener {
                welcomeDialog.dispose()
                showEditor()
            }
        }

        val createFileButtonConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            insets = Insets(10, 10, 10, 10)
        }
        buttonPanel.add(createFileButton, createFileButtonConstraints)

        val openExistingButton = JButton("Exit").apply {
            background = Color(100, 100, 100)
            foreground = WHITE
            border = RoundedBorder(10)
            addActionListener {
                exitProcess(0)
            }
        }
        val openExistingButtonConstraints = GridBagConstraints().apply {
            gridx = 1
            gridy = 0
            insets = Insets(10, 10, 10, 10)
        }
        buttonPanel.add(openExistingButton, openExistingButtonConstraints)

        welcomeDialog.add(buttonPanel, BorderLayout.SOUTH)

        welcomeDialog.isVisible = true
    }

    class RoundedBorder(private val arcRadius: Int) : Border {
        override fun paintBorder(
            c: Component,
            g: Graphics,
            x: Int,
            y: Int,
            width: Int,
            height: Int
        ) {
            val graphics2D = g as Graphics2D
            graphics2D.stroke = BasicStroke(2f)
            graphics2D.color = c.foreground
            graphics2D.drawRoundRect(x, y, width - 1, height - 1, arcRadius, arcRadius)
        }

        override fun getBorderInsets(c: Component): Insets {
            return Insets(4, 4, 4, 4)
        }

        override fun isBorderOpaque(): Boolean {
            return true
        }
    }

    private fun showEditor() {
        val frame = JFrame("Zephyr Ultimate 3.1.24")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.preferredSize = Dimension(1600, 900)
        frame.contentPane.background = WHITE

        frame.font = Font("Arial", Font.PLAIN, 14)

        val textArea = JTextArea()
        textArea.font = Font("Arial", Font.PLAIN, 14)
        textArea.foreground = WHITE
        textArea.background = Color(40, 42, 54)
        val scrollPane = JScrollPane(textArea)
        frame.add(scrollPane, BorderLayout.CENTER)

        val menuBar = createMenuBar(frame, textArea)
        menuBar.background = WHITE
        frame.jMenuBar = menuBar

        val iconURL = SwingTextEditor::class.java.getResource("icon.png")
        if (iconURL != null) {
            val icon = ImageIcon(iconURL)
            frame.iconImage = icon.image
            frame.iconImage = icon.image
        } else {
            println("Naw icon dont exist fam")
        }

        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    private fun showAboutDialog(frame: JFrame) {
        val aboutText = """
        Zephyr Ultimate 3.1.24
        A powerful text editor for all your coding needs.
        Developed by Zephyr Industries.
        """.trimIndent()

        JOptionPane.showMessageDialog(frame, aboutText, "About", JOptionPane.INFORMATION_MESSAGE)
    }

    private fun createMenuBar(frame: JFrame, textArea: JTextArea): JMenuBar {
        val menuBar = JMenuBar()
        menuBar.background = WHITE
        val fileMenu = JMenu("File")
        val openItem = JMenuItem("Open Project")
        openItem.addActionListener {
            val fileChooser = JFileChooser()
            val filter = FileNameExtensionFilter("All Code files", "txt", "bat", "cmd", "sh", "cpp", "java", "kt", "html", "css", "ts", "js", "py", "rb", "php", "swift", "go", "c", "csharp", "rust", "perl", "lua", "r", "scala", "dart", "shell", "sql", "xml", "json", "yaml", "toml", "asm", "v", "powershell", "groovy", "typescript", "jsx", "tsx", "scss", "sass", "less", "graphql", "protobuf", "protobuf3", "protobuf2", "nginx", "dockerfile", "makefile", "cmake", "ini", "csv", "markdown", "yaml", "groovy", "gradle", "swift", "vue", "tsx", "jsx", "rkt", "jl", "nim", "erl", "el", "clj", "fsharp", "dart", "d", "tsql", "sqlite", "plsql", "pgsql", "prolog", "tcl", "cabal", "haskell", "ada", "lisp", "fortran", "kotlin", "gd", "gdscript", "nim", "nimble", "pascal", "dylan", "alice", "oz", "scheme", "awk", "sed", "matlab", "octave", "powershell", "sh", "csh", "zsh", "bash", "fish", "tcsh", "ksh", "rc", "d", "dtrace", "ebnf", "glsl", "handlebars", "http", "idl", "javadoc", "json", "latex", "pegjs", "pgsql", "properties", "regexp", "sparql", "turtle", "velocity", "verilog", "vhdl", "wiki", "xquery", "yaml")
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
                menuBar.add(fileMenu)
            }
        }

        val saveItem = JMenuItem("Save Project")
        saveItem.addActionListener { saveToFile(textArea.text) }
        saveItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)

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
            updateThemeColors(textArea, Color(19, 4, 40), WHITE)
        }
        themeMenu.add(ZephyrlightThemeItem)
        themeMenu.add(ZephyrThemeItem)
        themeMenu.add(darkPurpleThemeItem)
        themeMenu.add(OLEDTheme)

        val aboutItem = JMenuItem("About")
        aboutItem.addActionListener {
            showAboutDialog(frame)
        }

        menuBar.add(fileMenu)
        menuBar.add(themeMenu)
        menuBar.add(aboutItem)

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

    private fun openWelcome() {
        val openItem = JMenuItem("Open Project")
        openItem.addActionListener {
            val fileChooser = JFileChooser()
            val filter = FileNameExtensionFilter("Text Files", "txt")
            fileChooser.fileFilter = filter
            val frame = JFrame()
            val result = fileChooser.showOpenDialog(frame)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedFile = fileChooser.selectedFile
                try {
                    val textArea = JTextArea()
                    val content = String(Files.readAllBytes(selectedFile.toPath()))
                    textArea.text = content
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
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
