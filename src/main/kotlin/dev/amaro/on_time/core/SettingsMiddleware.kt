package dev.amaro.on_time.core

import dev.amaro.on_time.Modules
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.ui.TextResources
import dev.amaro.on_time.utilities.Resources
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class SettingsMiddleware: IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (action is Actions.SaveConfiguration) {
            try {
                if (action.configuration.isValid) {
                    saveChanges(action.configuration)
                    processor.reduce(action)
                } else {
                    processor.reduce(Actions.ProvideFeedback(Feedback(TextResources.Errors.NotAllSettingsWereInformed, FeedbackType.Error)))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun saveChanges(configuration: Configuration) {
        val properties = Resources.loadConfigurationFile(Modules.CONFIGURATION_FILE)
        properties.setProperty("host", configuration.host)
        properties.setProperty("user", configuration.user)
        properties.setProperty("token", configuration.token)
        Resources.saveConfigurationFile(Modules.CONFIGURATION_FILE, properties)
    }
}