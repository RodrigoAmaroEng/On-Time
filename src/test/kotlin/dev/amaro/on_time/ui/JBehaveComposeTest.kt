package dev.amaro.on_time.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import dev.amaro.on_time.Modules
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.steps.ActionSteps
import dev.amaro.on_time.ui.steps.AssertionSteps
import dev.amaro.on_time.ui.steps.ContextSteps
import org.jbehave.core.configuration.Configuration
import org.jbehave.core.configuration.MostUsefulConfiguration
import org.jbehave.core.io.LoadFromRelativeFile
import org.jbehave.core.junit.JUnitStories
import org.jbehave.core.parsers.RegexStoryParser
import org.jbehave.core.reporters.Format
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.steps.InjectableStepsFactory
import org.jbehave.core.steps.InstanceStepsFactory
import org.junit.Before
import org.junit.Rule
import java.net.URL

abstract class JBehaveComposeTest: JUnitStories() {
    abstract val storyFile: String

    override fun configuration(): Configuration {
        return MostUsefulConfiguration()
            .useStoryParser(RegexStoryParser())
            .useStoryLoader(MyStoryLoader())
            .useStoryReporterBuilder(
                StoryReporterBuilder()
                .withDefaultFormats()
                .withFormats(Format.CONSOLE, Format.TXT))
    }

    override fun stepsFactory(): InjectableStepsFactory {
        return InstanceStepsFactory(configuration(), ContextSteps(), AssertionSteps(), ActionSteps())
    }

    companion object {
        var composer: ComposeContentTestRule? = null
        var initialState: AppState = AppState()
        val debugModules: MutableList<org.koin.core.module.Module> =  mutableListOf()
        var app: OnTimeApp? = null

        fun startApp(screen: ScreenConstructor) {
            app = OnTimeApp(Modules.release, *debugModules.toTypedArray())
            composer?.apply {
                setContent {
                    screen(initialState) { app!!.perform(it) }
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
        composer = composeRule
    }
}

class MyStoryLoader : LoadFromRelativeFile(URL("file://Users/amaro/Projects/OnTime/features"))

typealias ScreenConstructor = @Composable (AppState, (Actions) -> Unit) -> Unit