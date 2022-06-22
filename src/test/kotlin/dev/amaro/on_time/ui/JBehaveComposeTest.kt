package dev.amaro.on_time.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import dev.amaro.on_time.Modules
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.steps.ActionStepsForJ
import dev.amaro.on_time.ui.steps.AssertionStepsForJ
import dev.amaro.on_time.ui.steps.ContextStepsForJ
import org.jbehave.core.configuration.Configuration
import org.jbehave.core.configuration.MostUsefulConfiguration
import org.jbehave.core.io.LoadFromRelativeFile
import org.jbehave.core.junit.JUnitStories
import org.jbehave.core.steps.InjectableStepsFactory
import org.jbehave.core.steps.InstanceStepsFactory
import org.junit.Before
import org.junit.Rule
import java.net.URL

abstract class JBehaveComposeTest: JUnitStories() {
    abstract val storyFile: String

    override fun configuration(): Configuration {
        return MostUsefulConfiguration()
            .useStoryLoader(MyStoryLoader())
    }

    override fun stepsFactory(): InjectableStepsFactory {
        return InstanceStepsFactory(configuration(), ContextStepsForJ(), AssertionStepsForJ(), ActionStepsForJ())
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
        return mutableListOf(storyFile);
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