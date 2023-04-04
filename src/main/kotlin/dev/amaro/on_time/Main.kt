package dev.amaro.on_time

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Navigation
import dev.amaro.on_time.network.stream_deck.Server
import dev.amaro.on_time.ui.TextResources
import dev.amaro.on_time.ui.screens.MainScreen
import dev.amaro.on_time.ui.screens.SettingsScreen
import kotlinx.coroutines.launch


object WindowSetup {
    const val width = 1080
    const val height = 810
}

fun main(vararg params: String) = application {
    val parameters: Parameters = params.toParameters()
    val appInstance = OnTimeApp(AppState(), Modules.generateReleaseModule(parameters))
    val app = remember {
        mutableStateOf(appInstance)
    }
    val scopeForServer = rememberCoroutineScope()
    app.value.apply {
        initialize()
        val state = getState()
        Window(
            onCloseRequest = ::exitApplication,
            title = TextResources.Title,
            state = rememberWindowState(width = WindowSetup.width.dp, height = WindowSetup.height.dp),
            icon = painterResource("icon.iconset/icon_512x512.png"),
        ) {
            defineCurrentScreen(state.collectAsState().value)
        }
        scopeForServer.launch {
            Server.main( state) { perform(it) }
        }
    }
}

@Composable
fun OnTimeApp.defineCurrentScreen(state: AppState) {
    if (state.screen == Navigation.Main) {
        MainScreen(state) { perform(it) }
    } else if (state.screen == Navigation.Configuration) {
        SettingsScreen(state) { perform(it) }
    }
}

typealias Parameters = Map<String, String>

fun home(): String = System.getProperty("user.home")

fun Array<out String>.toParameters(): Parameters {
    return map { it.split("=") }.associate { Pair(it[0], it[1]) }
}

fun Parameters.resolve(key: String): String? {
    return get(key)?.replace("\$HOME", home())
}

const val FOLDER_PARAM_KEY = "folder"