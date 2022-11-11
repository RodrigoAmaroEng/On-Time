package dev.amaro.on_time.unit

import dev.amaro.on_time.Samples
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.SettingsMiddleware
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.utilities.ConfigurationManager
import dev.amaro.on_time.utilities.Resources
import dev.amaro.sonic.IProcessor
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlin.test.Test

class SettingsMiddlewareTest {


    @Test
    fun saveNewSettings() {
        mockkObject(Resources)
        val configurationManager: ConfigurationManager = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val settings = Configuration(Samples.HOST, Samples.USER, Samples.TOKEN)
        val action = Actions.SaveConfiguration(settings)
        val middleware = SettingsMiddleware(configurationManager)
        middleware.process(action, AppState(), processor)
        verify {
            configurationManager.save(settings)
        }
        verify {
            processor.reduce(eq(action))
        }
    }

    @Test
    fun `Error message if the configuration was not fully informed`() {
        mockkObject(Resources)
        val configurationManager: ConfigurationManager = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val settings = Configuration()
        val action = Actions.SaveConfiguration(settings)
        val middleware = SettingsMiddleware(configurationManager)
        middleware.process(action, AppState(), processor)
        verify(exactly = 0) {
            configurationManager.save( any())
        }
        verify {
            processor.reduce(any<Actions.ProvideFeedback>())
        }
    }
}