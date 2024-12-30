package dev.thoq.zephyr.views

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.canvas.Canvas
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

/**
 * A custom UI component that extends `StackPane`. The component includes a gradient background
 * and a close button aligned within an HBox layout structure. The background transitions between
 * two predefined colors and dynamically adjusts based on size changes of the canvas.
 */
class Bar : StackPane() {

    private val gradientStartColor: Color = Color.rgb(73, 26, 115)
    private val gradientEndColor: Color = Color.TRANSPARENT

    init {
        prefHeight = 40.0

        val background = Canvas()
        background.widthProperty().bind(widthProperty())
        background.heightProperty().bind(heightProperty())

        background.widthProperty().addListener { _, _, _ -> drawGradientBackground(background) }
        background.heightProperty().addListener { _, _, _ -> drawGradientBackground(background) }

        val closeButton = createRoundedButton("X", size = 25.0)

        val layout = HBox(closeButton).apply {
            alignment = Pos.CENTER_LEFT
            padding = Insets(5.0, 10.0, 5.0, 10.0)
        }

        children.addAll(background, layout)
    }

    /**
     * Fills the provided canvas with a gradient background transitioning between two defined colors.
     *
     * @param canvas The canvas on which the gradient background will be drawn.
     */
    private fun drawGradientBackground(canvas: Canvas) {
        val gc = canvas.graphicsContext2D
        val gradient = LinearGradient(
            0.0, 0.0, 1.0, 1.0, true,
            javafx.scene.paint.CycleMethod.NO_CYCLE,
            Stop(0.0, gradientStartColor),
            Stop(1.0, gradientEndColor)
        )
        gc.fill = gradient
        gc.fillRect(0.0, 0.0, canvas.width, canvas.height)
    }

    /**
     * Creates a rounded button with specified text and size.
     *
     * @param text The text to be displayed inside the button.
     * @param size The height and width of the button, which is a square.
     * @return*/
    private fun createRoundedButton(text: String, size: Double): StackPane {
        return StackPane().apply {
            prefWidth = size
            prefHeight = size

            style = """
                -fx-background-color: #9f73ea;
                -fx-background-radius: 4;
            """.trimIndent()

            val textNode = Text(text).apply {
                fill = Color.WHITE
                font = Font.font("Monospaced", FontWeight.BOLD, size / 1.6)
            }

            alignment = Pos.CENTER
            children.add(textNode)
        }
    }
}