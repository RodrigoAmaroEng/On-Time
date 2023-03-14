package dev.amaro.on_time.ui

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import dev.amaro.on_time.Modules
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.WindowSetup
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.defineCurrentScreen
import dev.amaro.on_time.log.Clock
import dev.amaro.on_time.log.Storage
import dev.amaro.on_time.log.TestSQLiteStorage
import dev.amaro.on_time.ui.compose.*
import dev.amaro.on_time.ui.spice.getFromField
import dev.amaro.sonic.IMiddleware
import io.cucumber.core.backend.TestCaseState
import io.cucumber.java.AfterStep
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME
import io.cucumber.plugin.event.PickleStepTestStep
import io.cucumber.plugin.event.TestCase
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.TestInstance
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import org.koin.dsl.module
import java.io.File


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("dev/amaro/on_time/ui")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "dev.amaro.on_time.ui")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RunCucumberTest  {


    @OptIn(ExperimentalTestApi::class, ExperimentalCoroutinesApi::class)
    companion object {
        val storage = spyk(TestSQLiteStorage())
        val clockScope = TestScope()
        var composer: DesktopComposeUiTest? = null
        var initialState: AppState = AppState()
        val debugModules: MutableList<org.koin.core.module.Module> = mutableListOf()
        var testContext: TestData? = null
        private var app: OnTimeApp? = null

        init {
            File("build/reports/screenshots").takeIf { it.exists() }?.deleteRecursively()
        }

        fun createEnvironment() {
            Dispatchers.setMain(Dispatchers.Swing)
            composer?.stop()
            app?.kill()
            composer = DesktopComposeUiTest(WindowSetup.width, WindowSetup.height).apply { start() }
        }

        fun startApp() {
            debugModules.add(TestModule)
            debugModules.add( module {
                single<Storage> { storage }
                single { Clock(clockScope)}
            })
            app = OnTimeApp(initialState, Modules.generateReleaseModule(emptyMap()), *debugModules.toTypedArray())
            app!!.initialize()
            composer?.run {
                setContent {
                    val composeApp = remember { mutableStateOf(app) }
                    val state = composeApp.value!!.getState().collectAsState().value
                    app?.defineCurrentScreen(state)
                }
                waitForIdle()
            }
        }
    }

    @Before
    fun onEach(scenario: Scenario) {
        val state: TestCaseState = scenario.getFromField("delegate") as TestCaseState
        val testCase: TestCase = state.getFromField("testCase") as TestCase
        testContext = TestData(scenario.name, testCase.testSteps.filterIsInstance<PickleStepTestStep>())
    }

    @OptIn(ExperimentalTestApi::class)
    @AfterStep
    fun onStep() {
        composer?.run {
            takeScreenshot(testContext)
        }
        testContext?.nextStep()
    }

}

val TestModule = module {
    single { params ->
        AppLogic(
            initialState = params.get(),
            debugMode = false,
            middlewares = get<Array<IMiddleware<AppState>>>()
        )
    }
}


