package dev.amaro.on_time.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import dev.amaro.on_time.Modules
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.log.TaskLogger
import dev.amaro.on_time.network.Connector
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.koin.dsl.module

open class ComposeTest {
    private val debugModule = module {
        single { mockk<Connector>() }
        single { mockk<TaskLogger>() }
    }
    private lateinit var app: OnTimeApp

    @Before
    fun setUp() {
        app = OnTimeApp(Modules.release, debugModule)
    }

    fun ComposeContentTestRule.setScreen(screen: ComposableScreen) {
        setContent {
            screen(app.getState()) { app.perform(it) }
        }
    }

    fun ui(
        compose: ComposeContentTestRule,
        testBody: suspend ComposeContentTestRule.() -> Unit
    ) {
        runBlocking(Dispatchers.Main) {
            compose.testBody()
        }
    }
}

typealias ComposableScreen = @Composable (AppState, Performer) -> Unit
typealias Performer = (Actions) -> Unit
