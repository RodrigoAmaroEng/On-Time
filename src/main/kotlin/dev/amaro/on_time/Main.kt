package dev.amaro.on_time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.ServiceMiddleware
import dev.amaro.on_time.network.JiraConnector
import dev.amaro.on_time.network.JiraRequester
import dev.amaro.on_time.ui.CurrentTask
import dev.amaro.on_time.ui.OnTimeColors
import dev.amaro.on_time.ui.OnTimeTheme
import dev.amaro.on_time.ui.TaskUI
import java.io.InputStream
import java.util.*

fun main() = application {
    val requester = JiraRequester(getProperty("host"), getProperty("cookie"))
    val connector = JiraConnector(requester)
    val middleware = ServiceMiddleware(connector)
//    val uiScope = rememberCoroutineScope()
    val appLogic = AppLogic(middleware)
    val stateHandler: State<AppState> = appLogic.listen().collectAsState()
    appLogic.perform(Actions.Refresh)
    stateHandler.value.apply {
        Window(
            onCloseRequest = ::exitApplication,
            title = "On Time - Task Manager",
            state = rememberWindowState(width = 500.dp, height = 300.dp),
        ) {
            OnTimeTheme {
                Surface(color = OnTimeColors.backgroundColor) {
                    Column {
                        currentTask?.let {
                            AnimatedVisibility(true) { CurrentTask(it) }
                        }
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(tasks) {
                                TaskUI(it) { task ->
                                    appLogic.perform(Actions.StartTask(task))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getProperty(name: String): String {
    return Properties().apply {
        load(Resources.load("/local.properties"))
    }.getProperty(name)
}

object Resources {
    fun load(path: String): InputStream? {
        return Resources::class.java.getResourceAsStream(path)
    }
}