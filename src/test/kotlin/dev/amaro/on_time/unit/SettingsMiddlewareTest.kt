package dev.amaro.on_time.unit

import dev.amaro.on_time.Modules
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.SettingsMiddleware
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.utilities.Resources
import dev.amaro.sonic.IProcessor
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlin.test.Test

class SettingsMiddlewareTest {
    @Test
    fun saveNewSettings() {
        mockkObject(Resources)
        every { Resources.saveConfigurationFile(any(), any()) } returns Unit
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val settings = Configuration()
        val action = Actions.SaveConfiguration(settings)
        val middleware = SettingsMiddleware()
        middleware.process(action, AppState(), processor)
        verify {
            Resources.saveConfigurationFile(eq(Modules.CONFIGURATION_FILE), any())
        }
        verify {
            processor.reduce(eq(action))
            processor.reduce(eq(Actions.Navigation.GoToMain))
        }
    }
}