@file:OptIn(InternalTestApi::class, ExperimentalCoroutinesApi::class, ExperimentalTestApi::class)

package dev.amaro.on_time.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ComposeScene
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.InternalTestApi
import dev.amaro.on_time.Modules
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.sonic.IMiddleware
import io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.koin.dsl.module


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("dev/amaro/on_time/ui")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "dev.amaro.on_time.ui")
class RunCucumberTest {

    companion object {
        var composer: DesktopComposeUiTest? = null
        var initialState: AppState = AppState()
        val debugModules: MutableList<org.koin.core.module.Module> = mutableListOf()
        var app: OnTimeApp? = null

        fun createEnvironment() {
            Dispatchers.setMain(Dispatchers.Swing)
            composer?.stop()
            app?.kill()
            composer = DesktopComposeUiTest().apply { start() }
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


fun DesktopComposeUiTest.start() {
    val createUi = javaClass.getDeclaredMethod("createUi").apply { isAccessible = true }
    scene = runOnUiThread { createUi.invoke(this@start) as ComposeScene }
}

fun DesktopComposeUiTest.stop() {
    val testScope: TestScope =
        javaClass.getDeclaredField("testScope").apply { isAccessible = true }.get(this) as TestScope
    val uncaughtExceptionHandler =
        javaClass.getDeclaredField("uncaughtExceptionHandler").apply { isAccessible = true }.get(this)
    val throwUncaught = uncaughtExceptionHandler.javaClass.getDeclaredMethod("throwUncaught")
    testScope.runTest { }
    runOnUiThread(scene::close)
    throwUncaught.invoke(uncaughtExceptionHandler)
}