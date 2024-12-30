package dev.thoq.zephyr

import dev.thoq.zephyr.utility.Io
import javafx.application.Application
import javafx.stage.Stage

class ZephyrApplication : Application() {
    override fun start(stage: Stage) {
        Io.println("Starting Zephyr...")
        SplashScreen().show {
            Io.println("Loading editor...")
            AppInitializer().showEditor(stage)
        }
    }
}

fun main() {
    Io.println("Welcome to Zephyr!")
    Application.launch(ZephyrApplication::class.java)
    Io.println("Goodbye!")
}