package dev.amaro.on_time

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import dev.amaro.on_time.ui.TaskUI
import dev.amaro.on_time.ui.Theme
import java.io.InputStream
import java.util.*

fun main() = application {
    val requester = JiraRequester(getProperty("host"), getProperty("cookie"))
    val connector = JiraConnector(requester)
    val tasks = connector.getTasks()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 500.dp, height = 300.dp)
    ) {
        Theme {
            LazyColumn(Modifier.fillMaxSize()) {
                items(tasks) {
                    TaskUI(it)
                }
            }
        }
    }
}

fun main2() {
    ServiceInterface().test()
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