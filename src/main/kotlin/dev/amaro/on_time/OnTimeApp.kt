package dev.amaro.on_time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.ServiceMiddleware
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.network.JiraConnector
import dev.amaro.on_time.network.JiraMapper
import dev.amaro.on_time.network.JiraRequester
import dev.amaro.on_time.network.JiraStateDefinition
import dev.amaro.on_time.utilities.Resources

class OnTimeApp {
    private val appLogic: AppLogic

    init {
        lateinit var user: String
        val requester = Resources.getConfiguration().let {
            user = it.getProperty("user")
            JiraRequester(it.getProperty("host"), it.getProperty("token"))
        }
        val jiraStateDefinition = buildMap {
            put(TaskState.NOT_STARTED, JiraStateDefinition(41, listOf("ToDo", "READY FOR DEVELOPMENT")))
            put(TaskState.WORKING, JiraStateDefinition(31, "IN PROGRESS"))
            put(TaskState.ON_REVIEW, JiraStateDefinition(51, "IN CODE REVIEW"))
            put(TaskState.ON_QA, JiraStateDefinition(101, listOf("READY FOR QA", "In QA")))
            put(TaskState.DONE, JiraStateDefinition(91, "Done"))
        }

        val connector = JiraConnector(requester, JiraMapper(jiraStateDefinition, user))
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

