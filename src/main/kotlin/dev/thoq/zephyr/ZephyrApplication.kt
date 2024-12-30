package dev.thoq.zephyr

import dev.thoq.zephyr.utility.misc.Io
import dev.thoq.zephyr.utility.misc.Zephyr
import javafx.application.Application
import javafx.stage.Stage

/**
 * Holds the name retrieved from the `Zephyr` utility class.
 * This value is used throughout the application to reference the name
 * associated with the Zephyr context or configuration.
 */
private val name = Zephyr.getName()

/**
 * ZephyrApplication is the main entry point of the application, responsible for bootstrapping
 * the initialization process and starting the user interface.
 *
 * The application begins by displaying a splash screen to the user, which includes a loading animation
 * or video. Once the splash screen sequence completes, it transitions to initializing and displaying
 * the main application interface.
 *
 * Overrides:
 * - `start(stage: Stage)`: Responsible for executing the application lifecycle. This method
 *   initializes the splash screen and executes required startup processes before rendering
 *   the main application.
 *
 * Key Steps in the Start Process:
 * 1. Log the initiation of the application using `Io.println`.
 * 2. Display the splash screen using `SplashScreen#show` and define a callback to execute
 *    once the splash screen has been completed.
 * 3. Upon splash screen completion, proceed with initializing and displaying the main editor
 *    interface via `AppInitializer#showEditor`.
 */
class ZephyrApplication : Application() {
    /**
     * Starts the application lifecycle by initializing the splash screen and transitioning
     * to the main editor interface upon completion.
     *
     * @param stage The primary stage for the application, used to hold and display the main editor interface.
     */
    override fun start(stage: Stage) {
        Io.println("Starting $name...")
        SplashScreen().show {
            Io.println("Loading editor...")
            AppInitializer().startApp(stage)
        }
    }
}

/**
 * The `main` function serves as the entry point of the application.
 *
 * Responsibilities:
 * - Prints a welcome message to the console indicating the application startup.
 * - Launches the `ZephyrApplication`, which initializes the splash screen and the main editor interface.
 * - Prints a farewell message to the console upon application exit.
 */
fun main() {
    Io.println("Welcome to $name!")
    Application.launch(ZephyrApplication::class.java)
    Io.println("Goodbye!")
}