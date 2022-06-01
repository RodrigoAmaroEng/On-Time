package dev.amaro.on_time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.ServiceMiddleware
import dev.amaro.on_time.network.JiraConnector
import dev.amaro.on_time.network.JiraRequester
import dev.amaro.on_time.utilities.Resources

class OnTimeApp {
    private val appLogic: AppLogic

    init {
        val requester = Resources.getConfiguration().let {
            JiraRequester(it.getProperty("host"), it.getProperty("token"))
        }
        val connector = JiraConnector(requester)
        val middleware = ServiceMiddleware(connector)
        appLogic = AppLogic(middleware)
    }

    private var hasStarted = false

    fun initialize() {
        if (!hasStarted)
            appLogic.perform(Actions.Refresh)
        hasStarted = true
    }

    fun perform(actions: Actions) {
        appLogic.perform(actions)
    }

    @Composable
    fun getState(): AppState = appLogic.listen().collectAsState().value
}