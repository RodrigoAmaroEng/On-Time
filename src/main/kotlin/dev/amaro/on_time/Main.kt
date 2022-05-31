package dev.amaro.on_time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.network.JiraConnector
import dev.amaro.on_time.network.JiraRequester
import dev.amaro.on_time.ui.ClockDisplay
import dev.amaro.on_time.ui.OnTimeColors
import dev.amaro.on_time.ui.TaskUI
import dev.amaro.on_time.ui.OnTimeTheme
import java.io.InputStream
import java.time.LocalDateTime
import java.util.*

fun main() = application {
    val requester = JiraRequester(getProperty("host"), getProperty("cookie"))
    val connector = JiraConnector(requester)
    val tasks = connector.getTasks()
    val current = remember { mutableStateOf<Task?>(null) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 500.dp, height = 300.dp),
    ) {
        OnTimeTheme {
            Surface(color = OnTimeColors.backgroundColor) {
                Column {
                    AnimatedVisibility(current.value != null) {
                        Row {
                            current.value?.apply {
                                Row {
                                    Text(id)
                                }
                            }
                            ClockDisplay(LocalDateTime.now())
                        }
                    }
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tasks) {
                            TaskUI(it) { task ->
                                println("Task: $task")
                                current.value = task
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
    fun load(path: String): InputStream {
        return Resources::class.java.getResourceAsStream(path)
    }
}