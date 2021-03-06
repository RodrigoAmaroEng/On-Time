package dev.amaro.on_time.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import dev.amaro.on_time.Modules
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.steps.ActionSteps
import dev.amaro.on_time.ui.steps.AssertionSteps
import dev.amaro.on_time.ui.steps.ContextSteps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.jbehave.core.annotations.BeforeScenario
import org.jbehave.core.configuration.Configuration
import org.jbehave.core.configuration.MostUsefulConfiguration
import org.jbehave.core.io.CodeLocations.codeLocationFromClass
import org.jbehave.core.io.LoadFromRelativeFile
import org.jbehave.core.junit.JUnitStories
import org.jbehave.core.parsers.RegexStoryParser
import org.jbehave.core.reporters.Format
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.steps.InjectableStepsFactory
import org.jbehave.core.steps.InstanceStepsFactory
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class JBehaveComposeTest : JUnitStories() {
    abstract val storyFile: String

    override fun configuration(): Configuration {
        return MostUsefulConfiguration()
            .useStoryParser(RegexStoryParser())
            .useStoryLoader(MyStoryLoader())
            .useStoryReporterBuilder(
                StoryReporterBuilder()
                    .withCodeLocation(codeLocationFromClass(this.javaClass))
                    .withDefaultFormats()
                    .withFormats(Format.HTML)
            )
    }

    override fun stepsFactory(): InjectableStepsFactory {
        return InstanceStepsFactory(configuration(), ContextSetup(), ContextSteps(), AssertionSteps(), ActionSteps())
    }

    companion object {
        var composer: ComposeContentTestRule? = null
        var initialState: AppState = AppState()
        val debugModules: MutableList<org.koin.core.module.Module> = mutableListOf()
        var app: OnTimeApp? = null

        fun startApp(screen: ScreenConstructor) {
            app = OnTimeApp(initialState, Modules.release, *debugModules.toTypedArray())
            println(" # Initial State: $initialState")
            app!!.initialize()
            composer?.apply {
                setContent {
                    val composeApp = remember { mutableStateOf(app) }
                    println(" # Compose State: ${composeApp.value!!.getState()}")
                    screen(composeApp.value!!.getState()) { app!!.perform(it) }
                }
                waitForIdle()
            }
        }
    }

    override fun storyPaths(): MutableList<String> {
        return mutableListOf(storyFile)
    }

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        composer = composeRule
    }

    @After
    fun dispose() {
        Dispatchers.resetMain()
    }

    class ContextSetup() {
        @BeforeScenario
        fun tearDown() {
            debugModules.clear()
            app?.kill()
        }

    }

}

class MyStoryLoader : LoadFromRelativeFile(MyStoryLoader::class.java.classLoader.getResource("features"))

typealias ScreenConstructor = @Composable (AppState, (Actions) -> Unit) -> Unit

