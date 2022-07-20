@file:OptIn(InternalTestApi::class, ExperimentalCoroutinesApi::class)

package dev.amaro.on_time.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ComposeScene
import androidx.compose.ui.test.InternalTestApi
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.DesktopComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import dev.amaro.on_time.Modules
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.sonic.IMiddleware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.test.setMain
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.Suite
import org.koin.dsl.module


@Suite
@IncludeEngines("cucumber")
class RunCucumberTest {

    companion object {
        var composer: ComposeContentTestRule? = null
        var initialState: AppState = AppState()
        val debugModules: MutableList<org.koin.core.module.Module> = mutableListOf()
        var app: OnTimeApp? = null

        fun createEnvironment() {
            Dispatchers.setMain(Dispatchers.Swing)
            composer?.stop()
            app?.kill()
            composer = createComposeRule().apply { start() }
        }

        fun startApp(screen: ScreenConstructor) {
            debugModules.add(TestModule)
            app = OnTimeApp(initialState, Modules.release, *debugModules.toTypedArray())
            println(" # Initial State: $initialState")
            app!!.initialize()
            composer?.run{
                setContent {
                    val composeApp = remember { mutableStateOf(app) }
                    println(" # Compose State: ${composeApp.value!!.getState()}")
                    screen(composeApp.value!!.getState()) { app!!.perform(it) }
                }
                waitForIdle()
            }
        }
    }
}

typealias ScreenConstructor = @Composable (AppState, (Actions) -> Unit) -> Unit

val TestModule = module {
    single { params ->
        AppLogic(
            initialState = params.get(),
            debugMode = true,
            *get<Array<IMiddleware<AppState>>>()
        )
    }
}


fun DesktopComposeTestRule.start() {
    val createUi = javaClass.getDeclaredMethod("createUi").apply { isAccessible = true }
    scene = runOnUiThread { createUi.invoke(this@start) as ComposeScene }
}

fun DesktopComposeTestRule.stop() {
    val coroutineDispatcher =
        javaClass.getDeclaredField("coroutineDispatcher").apply { isAccessible = true }
            .get(this)
    val cleanupTestCoroutines =coroutineDispatcher.javaClass.getDeclaredMethod("cleanupTestCoroutines")
    val uncaughtExceptionHandler =
        javaClass.getDeclaredField("uncaughtExceptionHandler").apply { isAccessible = true }.get(this)
    val throwUncaught = uncaughtExceptionHandler.javaClass.getDeclaredMethod("throwUncaught")
    runOnUiThread(scene::close)
    cleanupTestCoroutines.invoke(coroutineDispatcher)
    throwUncaught.invoke(uncaughtExceptionHandler)
}


fun ComposeContentTestRule.stop() {
    return (this as DesktopComposeTestRule).stop()
}

fun ComposeContentTestRule.start() {
    return (this as DesktopComposeTestRule).start()
}