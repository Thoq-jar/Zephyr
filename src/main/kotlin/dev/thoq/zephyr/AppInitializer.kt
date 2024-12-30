package dev.thoq.zephyr

import javafx.stage.Stage
import dev.thoq.zephyr.views.MainMenu
class AppInitializer {
    private val mainMenu = MainMenu()

    fun startApp(stage: Stage) {
        mainMenu.show(stage)
    }
}