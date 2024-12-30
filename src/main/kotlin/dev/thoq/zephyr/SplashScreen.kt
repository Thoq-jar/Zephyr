package dev.thoq.zephyr

import dev.thoq.zephyr.utility.Zephyr
import javafx.animation.FadeTransition
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import dev.thoq.zephyr.utility.ui.ZScreen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import java.util.*

/**
 * The `SplashScreen` class is responsible for displaying a splash screen upon application startup.
 * It uses a media player to play a video and provides a visual splash screen experience.
 *
 * Features:
 * - Displays a splash video located at a predefined path.
 * - Allows the splash screen window to be dragged and repositioned using the mouse.
 * - Centers the splash screen on the current screen based on mouse position.
 * - Fades in the splash screen and closes it after a delay, executing a completion callback.
 *
 * Methods:
 * - `show(onSplashComplete: () -> Unit)`: Displays the splash screen, plays the splash video,
 *   and invokes the provided callback after the splash screen duration ends.
 */
class SplashScreen {
    private val io = Zephyr.io
    private var xOffset = 0.0
    private var yOffset = 0.0

    /**
     * Displays a splash screen with a video animation and a fade-in effect, and invokes the provided callback
     * once the splash screen is closed.
     *
     * @param onSplashComplete Function to be executed when the splash screen animation completes.
     */
    fun show(onSplashComplete: () -> Unit) {
        val videoPath = SplashScreen::class.java.getResource("/boot/splash.mp4")
            ?: io.warn("Splash screen failure: Could not find the splash video!")
        val media = Media(videoPath.toString())
        val mediaPlayer = MediaPlayer(media)
        val mediaView = MediaView(mediaPlayer)

        mediaView.isPreserveRatio = true
        mediaView.fitWidth = 640.0
        mediaView.fitHeight = 360.0

        val splashRoot = StackPane(mediaView)
        splashRoot.style = """
            -fx-background-radius: 5; 
            -fx-border-radius: 5; 
            -fx-background-color: transparent; 
        """.trimIndent()

        val splashScene = Scene(splashRoot, 640.0, 360.0)
        splashScene.fill = Color.TRANSPARENT

        val splashStage = Stage(StageStyle.TRANSPARENT)
        splashStage.scene = splashScene
        splashStage.isAlwaysOnTop = true

        splashRoot.setOnMousePressed { event: MouseEvent ->
            xOffset = event.sceneX
            yOffset = event.sceneY
        }
        splashRoot.setOnMouseDragged { event: MouseEvent ->
            splashStage.x = event.screenX - xOffset
            splashStage.y = event.screenY - yOffset
        }

        val mouseScreenBounds = ZScreen.getCurrentMouseScreenBounds()
        splashStage.x = mouseScreenBounds.minX + (mouseScreenBounds.width - splashScene.width) / 2
        splashStage.y = mouseScreenBounds.minY + (mouseScreenBounds.height - splashScene.height) / 2

        splashStage.show()

        mediaPlayer.play()

        Platform.runLater {
            val fadeIn = FadeTransition(Duration.seconds(0.5), splashRoot)
            fadeIn.fromValue = 0.0
            fadeIn.toValue = 1.0
            fadeIn.setOnFinished {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        Platform.runLater {
                            splashStage.close()
                            onSplashComplete()
                        }
                    }
                }, 4000)
            }
            fadeIn.play()
        }
    }
}