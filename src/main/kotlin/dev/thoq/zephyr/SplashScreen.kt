package dev.thoq.zephyr

import dev.thoq.zephyr.utility.Io
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.paint.Color
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.awt.MouseInfo
import java.util.*

class SplashScreen {
    private var xOffset = 0.0
    private var yOffset = 0.0

    fun show(onSplashComplete: () -> Unit) {
        val videoPath = SplashScreen::class.java.getResource("/boot/splash.mp4")
            ?: Io.warn("Splash screen failure: Could not find the splash video!")
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

        val mouseScreenBounds = getCurrentMouseScreenBounds()
        splashStage.x = mouseScreenBounds.minX + (mouseScreenBounds.width - splashScene.width) / 2
        splashStage.y = mouseScreenBounds.minY + (mouseScreenBounds.height - splashScene.height) / 2

        splashStage.show()

        mediaPlayer.play()

        Timer().schedule(object : TimerTask() {
            override fun run() {
                Platform.runLater {
                    splashStage.close()
                    onSplashComplete()
                }
            }
        }, 5000)
    }

    private fun getCurrentMouseScreenBounds(): javafx.geometry.Rectangle2D {
        val mouseLocation = MouseInfo.getPointerInfo().location
        val screen = Screen.getScreensForRectangle(
            mouseLocation.x.toDouble(),
            mouseLocation.y.toDouble(),
            1.0,
            1.0
        ).firstOrNull() ?: Screen.getPrimary()
        return screen.bounds
    }
}