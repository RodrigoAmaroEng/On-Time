package dev.amaro.on_time.core

import dev.amaro.on_time.ui.TextResources
import dev.amaro.on_time.utilities.ConfigurationManager
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class SettingsMiddleware(private val configurationManager: ConfigurationManager): IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (action is Actions.SaveConfiguration) {
            try {
                if (action.configuration.isValid) {
                    configurationManager.save(action.configuration)
                    processor.reduce(action)
                } else {
                    processor.reduce(Actions.ProvideFeedback(Feedback(TextResources.Errors.NotAllSettingsWereInformed, FeedbackType.Error)))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}