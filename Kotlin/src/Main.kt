import java.awt.*
import java.awt.Color.BLACK
import java.awt.Color.WHITE
import java.awt.event.*
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.swing.*
import javax.swing.border.Border
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.text.*
import javax.swing.undo.UndoManager
import kotlin.system.exitProcess


object Zephyr {
    private const val ZephyrLIGHT_THEME = "Zephyr Light"
    private const val Zephyr_THEME = "Zephyr Dark"
    private const val DARK_PURPLE_THEME: String = "Dark Purple"
    private const val OLED_THEME = "OLED"
    private const val RPM_THEME = "Rosé Pine Moon"
    private const val SPLASH_FILE_NAME = "splash.png"
    private const val ICON_FILE_NAME = "icon.png"
    private const val SPLASH_DURATION_MS = 4000

    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater {
            val splashURL = Zephyr::class.java.getResource(SPLASH_FILE_NAME)

            if (splashURL == null) {
                println("Splash image file not found: $SPLASH_FILE_NAME")
                return@invokeLater
            }

            val splashIcon = ImageIcon(splashURL)
            val loadingFrame = JFrame("Zephyr 3.1.24")
            loadingFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            loadingFrame.isUndecorated = true
            loadingFrame.background = Color(0, 0, 0, 0)
            loadingFrame.add(JLabel(splashIcon), BorderLayout.CENTER)
            loadingFrame.pack()
            loadingFrame.setLocationRelativeTo(null)

            val iconURL = Zephyr::class.java.getResource("icon.png")
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

    fun terminal() {
    val osName = System.getProperty("os.name").toLowerCase()
        val command: String

        when {
            osName.contains("win") -> {
                command = "cmd.exe"
            }
            osName.contains("mac") -> {
                command = "open -a terminal"
            }
            osName.contains("nix") || osName.contains("nux") || osName.contains("sunos") -> {
                command = "xterm"
            }
            else -> {
                println("Unsupported operating system: $osName")
                return
            }
        }

        try {
            val process = ProcessBuilder(command).start()
            println("Terminal opened successfully.")
        } catch (e: IOException) {
            println("Failed to open terminal: ${e.message}")
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
                showEditor()
            }
        }
        val openTerminalButton = JButton("Terminal").apply {
            background = Color(100, 100, 100)
            foreground = WHITE
            border = RoundedBorder(10)
            addActionListener {
            terminal()
        }
    }


        val createFileButtonSyntax = JButton("New Project (Syntax Recognition)").apply {
            background = Color(100, 100, 100)
            foreground = WHITE
            border = RoundedBorder(10)
            addActionListener {
                showEditorSyntax()
            }
        }

        val createFileButtonConstraints = GridBagConstraints().apply {
            gridx = 10
            gridy = 0
            insets = Insets(10, 10, 10, 10)
        }
        buttonPanel.add(createFileButton, createFileButtonConstraints)

        val openTerminalButtonConstraints = GridBagConstraints().apply {
            gridx = -10
            gridy = 0
            insets = Insets(10, 10, 10, 10)
        }
        buttonPanel.add(openTerminalButton, openTerminalButtonConstraints)

        val createFileButtonSyntaxConstants = GridBagConstraints().apply {
            gridx = 30
            gridy = 0
            insets = Insets(10, 10, 10, 10)
        }

        buttonPanel.add(createFileButtonSyntax, createFileButtonSyntaxConstants)

        val openExistingButton = JButton("Exit").apply {
            background = Color(100, 100, 100)
            foreground = WHITE
            border = RoundedBorder(10)
            addActionListener {
            welcomeDialog.dispose()
                exitProcess(0)
            }
        }
        val openExistingButtonConstraints = GridBagConstraints().apply {
            gridx = 0
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

        frame.font = Font("Arial", Font.PLAIN, 18)

        val textPane = JTextPane()
        textPane.font = Font("Arial", Font.PLAIN, 18)
        textPane.background = Color(40, 42, 54)
        textPane.foreground = WHITE

        // UndoManager for handling undo and redo actions
        val undoManager = UndoManager()
        val doc = DefaultStyledDocument(StyleContext.getDefaultStyleContext())
        textPane.document = doc
        doc.addUndoableEditListener { e ->
            undoManager.addEdit(e.edit)
        }

        val scrollPane = JScrollPane(textPane)
        frame.add(scrollPane, BorderLayout.CENTER)

        // Key bindings for macOS
        val inputMap = textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        val actionMap = textPane.actionMap

        // Undo action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().menuShortcutKeyMask), "undo")
        actionMap.put("undo", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                if (undoManager.canUndo()) {
                    undoManager.undo()
                }
            }
        })

        // Redo action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().menuShortcutKeyMask or InputEvent.SHIFT_DOWN_MASK), "redo")
        actionMap.put("redo", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                if (undoManager.canRedo()) {
                    undoManager.redo()
                }
            }
        })

        // Save action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().menuShortcutKeyMask), "save")
        actionMap.put("save", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                saveToFile(textPane.text)
            }
        })

       // textPane.document.addDocumentListener(object : DocumentListener {
       //             override fun insertUpdate(e: DocumentEvent) {
        //                autosave(textPane.text)
         //           }

        // Open action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().menuShortcutKeyMask), "open")
        actionMap.put("open", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                openFile(textPane)
            }
        })

        // Apply syntax highlighting
        val keywords = setOf(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "continue",
            "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package",
            "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
            "fun", "override", "when", "sealed", "internal", "crossinline", "noinline", "reified",
            "dynamic", "suspend", "operator", "companion", "infix", "in", "out", "vararg", "data",
            "const", "expect", "actual", "where", "annotation", "lateinit", "inline", "tailrec", "init",
            "typealias", "field", "property", "by", "get", "set", "delegate", "package", "catch", "finally",
            "try", "throw", "rethrow", "break", "continue", "return", "as", "is", "as?", "null", "super",
            "this", "when", "object", "if", "else", "while", "do", "for", "in", "typeof", "dynamic", "val",
            "var", "char", "byte", "short", "int", "long", "float", "double", "boolean", "true", "false", "string",
            "echo", "fi", "@", "Hello, world!", "Hello, World!", "Why is this highlighted?"
        )

        val excludedKeywords = setOf("about", "and", "or", "the", "a", "an", "is", "in", "on", "at", "to", "from", "with", "for", "of")
        val keywordsToHighlight = keywords - excludedKeywords
        val keywordPattern = Pattern.compile("\\b(${keywordsToHighlight.joinToString("|")})\\b(?=\\W)")
        val stringLiteralPattern = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"")


        val keywordColor = Color(255, 153, 0) // Orange
        val stringLiteralColor = Color(0, 153, 255) // Blue
        val numberLiteralColor = Color(51, 130, 255) // Light Blue
        val commentColor = Color.GRAY
        val textPaneColor = WHITE

       // applySyntaxHighlighting(textPane, textPaneColor, keywords, keywordColor, stringLiteralColor, numberLiteralColor, commentColor)

        val menuBar = createMenuBar(frame, textPane)
        menuBar.background = WHITE
        frame.jMenuBar = menuBar

        val iconURL = Zephyr::class.java.getResource("icon.png")
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

    private fun showEditorSyntax() {
        val frame = JFrame("Zephyr Ultimate 3.1.24")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.preferredSize = Dimension(1600, 900)
        frame.contentPane.background = WHITE

        frame.font = Font("Arial", Font.PLAIN, 18)

        val textPane = JTextPane()
        textPane.font = Font("Arial", Font.PLAIN, 18)
        textPane.background = Color(40, 42, 54)
        textPane.foreground = WHITE

        // UndoManager for handling undo and redo actions
        val undoManager = UndoManager()
        val doc = DefaultStyledDocument(StyleContext.getDefaultStyleContext())
        textPane.document = doc
        doc.addUndoableEditListener { e ->
            undoManager.addEdit(e.edit)
        }

        val scrollPane = JScrollPane(textPane)
        frame.add(scrollPane, BorderLayout.CENTER)

        // Key bindings for macOS
        val inputMap = textPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        val actionMap = textPane.actionMap

        // Undo action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().menuShortcutKeyMask), "undo")
        actionMap.put("undo", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                if (undoManager.canUndo()) {
                    undoManager.undo()
                }
            }
        })

        // Redo action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().menuShortcutKeyMask or InputEvent.SHIFT_DOWN_MASK), "redo")
        actionMap.put("redo", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                if (undoManager.canRedo()) {
                    undoManager.redo()
                }
            }
        })

        // Save action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().menuShortcutKeyMask), "save")
        actionMap.put("save", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                saveToFile(textPane.text)
            }
        })

        // Open action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().menuShortcutKeyMask), "open")
        actionMap.put("open", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                openFile(textPane)
            }
        })

       // textPane.document.addDocumentListener(object : DocumentListener {
        //            override fun insertUpdate(e: DocumentEvent) {
        //                autosave(textPane.text)
         //           }

        // Apply syntax highlighting
        val keywords = setOf(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "continue",
            "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "if",
            "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "package",
            "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
            "fun", "override", "when", "sealed", "internal", "crossinline", "noinline", "reified",
            "dynamic", "suspend", "operator", "companion", "infix", "in", "out", "vararg", "data",
            "const", "expect", "actual", "where", "annotation", "lateinit", "inline", "tailrec", "init",
            "typealias", "field", "property", "by", "get", "set", "delegate", "package", "catch", "finally",
            "try", "throw", "rethrow", "break", "continue", "return", "as", "is", "as?", "null", "super",
            "this", "when", "object", "if", "else", "while", "do", "for", "in", "typeof", "dynamic", "val",
            "var", "char", "byte", "short", "int", "long", "float", "double", "boolean", "true", "false", "string",
            "echo", "fi", "@", "Hello, world!", "Hello, World!", "Why is this highlighted?",
        )

        val excludedKeywords = setOf("about", "and", "or", "the", "a", "an", "is", "in", "on", "at", "to", "from", "with", "for", "of")
        val keywordsToHighlight = keywords - excludedKeywords
        val keywordPattern = Pattern.compile("\\b(${keywordsToHighlight.joinToString("|")})\\b(?=\\W)")
        val stringLiteralPattern = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"<--`")


        val keywordColor = Color(255, 153, 0) // Orange
        val stringLiteralColor = Color(0, 153, 255) // Blue
        val numberLiteralColor = Color(51, 130, 255) // Light Blue
        val commentColor = Color.GRAY
        val textPaneColor = WHITE

        applySyntaxHighlighting(textPane, textPaneColor, keywords, keywordColor, stringLiteralColor, numberLiteralColor, commentColor)

        val menuBar = createMenuBar(frame, textPane)
        menuBar.background = WHITE
        frame.jMenuBar = menuBar

        val iconURL = Zephyr::class.java.getResource("icon.png")
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

    private fun createMenuBar(frame: JFrame, textArea: JTextPane): JMenuBar {
        val menuBar = JMenuBar()
        menuBar.background = WHITE
        val fileMenu = JMenu("File")
        val openItem = JMenuItem("Open Project")
        openItem.addActionListener {
            openFile(textArea)
        }

        val saveItem = JMenuItem("Save Project")
        saveItem.addActionListener { saveToFile(textArea.text) }
        saveItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)

        openItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)
        fileMenu.add(openItem)
        fileMenu.add(saveItem)
        menuBar.add(fileMenu)

        val themeMenu = JMenu("Theme")
        val themeGroup = ButtonGroup()
        val ZephyrlightThemeItem = JRadioButtonMenuItem(ZephyrLIGHT_THEME)
        true.also { it.also { ZephyrlightThemeItem.isSelected = it } }
        val ZephyrThemeItem = JRadioButtonMenuItem(Zephyr_THEME)
        val darkPurpleThemeItem = JRadioButtonMenuItem(DARK_PURPLE_THEME)
        val OLEDTheme = JRadioButtonMenuItem(OLED_THEME)
        val RPMTheme = JRadioButtonMenuItem(RPM_THEME)
        themeGroup.add(ZephyrlightThemeItem)
        themeGroup.add(ZephyrThemeItem)
        themeGroup.add(darkPurpleThemeItem)
        ZephyrlightThemeItem.addActionListener {
            setTheme(frame, UIManager.getCrossPlatformLookAndFeelClassName())
            updateThemeColors(textArea, WHITE, BLACK)
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
       RPMTheme.addActionListener {
           setTheme(frame, UIManager.getSystemLookAndFeelClassName())
           updateThemeColors(textArea, Color(35, 33, 54), Color(224, 222, 244))
       }
        themeMenu.add(ZephyrlightThemeItem)
        themeMenu.add(ZephyrThemeItem)
        themeMenu.add(RPMTheme)
        themeMenu.add(darkPurpleThemeItem)
        themeMenu.add(OLEDTheme)

        val aboutItem = JMenuItem("About")
        aboutItem.addActionListener {
            showAboutDialog(frame)
        }

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

    private fun applySyntaxHighlighting(
        textPane: JTextPane,
        textPaneColor: Color,
        keywords: Set<String>,
        keywordColor: Color,
        stringLiteralColor: Color,
        numberLiteralColor: Color,
        commentColor: Color
    ) {
        val doc = textPane.styledDocument
        val defaultStyle = doc.addStyle("Default", null)
        StyleConstants.setForeground(defaultStyle, Color.BLACK) // Set default text color

        val keywordStyle = doc.addStyle("Keyword", null)
        StyleConstants.setForeground(keywordStyle, keywordColor)

        val stringLiteralStyle = doc.addStyle("StringLiteral", null)
        StyleConstants.setForeground(stringLiteralStyle, stringLiteralColor)

        val numberLiteralStyle = doc.addStyle("NumberLiteral", null)
        StyleConstants.setForeground(numberLiteralStyle, numberLiteralColor)

        val commentStyle = doc.addStyle("Comment", null)
        StyleConstants.setForeground(commentStyle, commentColor)

        textPane.document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent) {
                updateSyntaxHighlighting(e)
            }

            override fun removeUpdate(e: DocumentEvent) {
                updateSyntaxHighlighting(e)
            }

            override fun changedUpdate(e: DocumentEvent) {
                // Plain text components do not fire these events
            }

            private fun updateSyntaxHighlighting(e: DocumentEvent) {
                SwingUtilities.invokeLater {
                    try {
                        // Reset the entire document to default style
                        doc.setCharacterAttributes(0, doc.length, defaultStyle, true)

                        // Highlight keywords
                        for (keyword in keywords) {
                            var startIndex = 0
                            while ((doc.getText(0, doc.length).indexOf(keyword, startIndex).also {
                                    startIndex = it
                                }) != -1) {
                                val endIndex = startIndex + keyword.length
                                doc.setCharacterAttributes(startIndex, endIndex - startIndex, keywordStyle, false)
                                startIndex = endIndex
                            }
                        }

                        // Highlight string literals
                        val stringLiteralPattern =
                            Pattern.compile("\"([^\"\\\\]|\\\\.)*\"")
                        val stringLiteralMatcher: Matcher = stringLiteralPattern.matcher(doc.getText(0, doc.length))
                        while (stringLiteralMatcher.find()) {
                            doc.setCharacterAttributes(
                                stringLiteralMatcher.start(),
                                stringLiteralMatcher.end() - stringLiteralMatcher.start(),
                                stringLiteralStyle,
                                false
                            )
                        }

                        // Highlight number literals
                        val numberLiteralPattern = Pattern.compile("\\b\\d+\\b")
                        val numberLiteralMatcher: Matcher = numberLiteralPattern.matcher(doc.getText(0, doc.length))
                        while (numberLiteralMatcher.find()) {
                            doc.setCharacterAttributes(
                                numberLiteralMatcher.start(),
                                numberLiteralMatcher.end() - numberLiteralMatcher.start(),
                                numberLiteralStyle,
                                false
                            )
                        }

                        // Highlight comments
                        val commentPattern = Pattern.compile("//.*")
                        val commentMatcher: Matcher = commentPattern.matcher(doc.getText(0, doc.length))
                        while (commentMatcher.find()) {
                            doc.setCharacterAttributes(
                                commentMatcher.start(),
                                commentMatcher.end() - commentMatcher.start(),
                                commentStyle,
                                false
                            )
                        }
                    } catch (e: BadLocationException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    private fun updateThemeColors(textArea: JTextPane, backgroundColor: Color, foregroundColor: Color) {
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

    private fun openFile(textArea: JTextPane) {
        val fileChooser = JFileChooser()
        val filter = FileNameExtensionFilter("All Code files", "txt", "bat", "cmd", "sh", "cpp", "java", "kt", "html", "css", "ts", "js", "py", "rb", "php", "swift", "go", "c", "csharp", "rust", "perl", "lua", "r", "scala", "dart", "shell", "sql", "xml", "json", "yaml", "toml", "asm", "v", "powershell", "groovy", "typescript", "jsx", "tsx", "scss", "sass", "less", "graphql", "protobuf", "protobuf3", "protobuf2", "nginx", "dockerfile", "makefile", "cmake", "ini", "csv", "markdown", "yaml", "groovy", "gradle", "swift", "vue", "tsx", "jsx", "rkt", "jl", "nim", "erl", "el", "clj", "fsharp", "dart", "d", "tsql", "sqlite", "plsql", "pgsql", "prolog", "tcl", "cabal", "haskell", "ada", "lisp", "fortran", "kotlin", "gd", "gdscript", "nim", "nimble", "pascal", "dylan", "alice", "oz", "scheme", "awk", "sed", "matlab", "octave", "powershell", "sh", "csh", "zsh", "bash", "fish", "tcsh", "ksh", "rc", "d", "dtrace", "ebnf", "glsl", "handlebars", "http", "idl", "javadoc", "json", "latex", "pegjs", "pgsql", "properties", "regexp", "sparql", "turtle", "velocity", "verilog", "vhdl", "wiki", "xquery", "yaml")
        fileChooser.fileFilter = filter
        val result = fileChooser.showOpenDialog(null)
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

    private fun highlightRegex(textPane: JTextPane, regex: String, style: Style) {
        val doc = textPane.styledDocument
        val text = textPane.text
        val pattern = regex.toRegex()
        val matches = pattern.findAll(text)

        matches.forEach {
            val range = it.range
            doc.setCharacterAttributes(range.start, range.endInclusive - range.start + 1, style, false)
        }
    }

    private fun colorToHex(color: Color): String {
        val red = Integer.toHexString(color.red)
        val green = Integer.toHexString(color.green)
        val blue = Integer.toHexString(color.blue)
        return "#${pad(red)}${pad(green)}${pad(blue)}"
    }

    private fun pad(hex: String): String {
        return if (hex.length == 1) "0$hex" else hex
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
}
    private fun applySyntaxHighlighting(
        textPane: JTextPane,
        textPaneColor: Color,
        keywords: Set<String>,
        keywordColor: Color,
        stringLiteralColor: Color,
        numberLiteralColor: Color,
        commentColor: Color,
        isHighlightingEnabled: Boolean
    ) {
        val doc = textPane.styledDocument
        val defaultStyle = doc.addStyle("Default", null)
        StyleConstants.setForeground(defaultStyle, textPaneColor)

        if (!isHighlightingEnabled) {
            doc.setCharacterAttributes(0, doc.length, defaultStyle, true)
            return
        }

        // Define styles for different syntax elements
        val keywordStyle = doc.addStyle("Keyword", null)
        StyleConstants.setForeground(keywordStyle, keywordColor)

        val stringLiteralStyle = doc.addStyle("StringLiteral", null)
        StyleConstants.setForeground(stringLiteralStyle, stringLiteralColor)

        val numberLiteralStyle = doc.addStyle("NumberLiteral", null)
        StyleConstants.setForeground(numberLiteralStyle, numberLiteralColor)

        val commentStyle = doc.addStyle("Comment", null)
        StyleConstants.setForeground(commentStyle, commentColor)

        // Highlight keywords
        val keywordPattern = Pattern.compile("\\b(${keywords.joinToString("|")})\\b(?=\\W)")
        val keywordMatcher = keywordPattern.matcher(textPane.text)
        while (keywordMatcher.find()) {
            doc.setCharacterAttributes(
                keywordMatcher.start(),
                keywordMatcher.end() - keywordMatcher.start(),
                keywordStyle,
                false
            )
        }

        // Highlight string literals
        val stringLiteralPattern = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"")
        val stringLiteralMatcher = stringLiteralPattern.matcher(textPane.text)
        while (stringLiteralMatcher.find()) {
            doc.setCharacterAttributes(
                stringLiteralMatcher.start(),
                stringLiteralMatcher.end() - stringLiteralMatcher.start(),
                stringLiteralStyle,
                false
            )
        }

        // Highlight number literals
        val numberLiteralPattern = Pattern.compile("\\b\\d+\\b(?=\\W)")
        val numberLiteralMatcher = numberLiteralPattern.matcher(textPane.text)
        while (numberLiteralMatcher.find()) {
            doc.setCharacterAttributes(
                numberLiteralMatcher.start(),
                numberLiteralMatcher.end() - numberLiteralMatcher.start(),
                numberLiteralStyle,
                false
            )
        }

      //  private fun autosave(content: String) {
         //   try {
       //         // Get the current working directory of the JVM
          //      val currentDirectory = System.getProperty("user.dir")
               // Construct the file path using the current directory
            //    val filePath = "$currentDirectory/autosave.txt"
            //    FileWriter(filePath).use { writer ->
            //        writer.write(content)
            //        println("Autosaved to $filePath")
             //   }
          // } catch (e: IOException) {
          //      e.printStackTrace()
          //  }
      //  }

        // fun applyRosePineMoonTheme(textPane: JTextPane) {
        //    textPane.background = Color.decode("#232136")
        //    textPane.foreground = Color.decode("#e0def4")
        // }

        // Highlight comments
        val commentPattern = Pattern.compile("//.*#<---->")
        val commentMatcher = commentPattern.matcher(textPane.text)
        while (commentMatcher.find()) {
            doc.setCharacterAttributes(
                commentMatcher.start(),
                commentMatcher.end() - commentMatcher.start(),
                commentStyle,
                false
            )
        }
    }
