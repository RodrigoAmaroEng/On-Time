package dev.amaro.on_time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Navigation
import dev.amaro.on_time.ui.TextResources
import dev.amaro.on_time.ui.screens.MainScreen
import dev.amaro.on_time.ui.screens.SettingsScreen


object WindowSetup {
    const val width = 500
    const val height = 300
}

fun main() = application {
    val appInstance = OnTimeApp(AppState(), Modules.release)
    val app = remember {
        mutableStateOf(appInstance)
    }
    app.value.apply {
        initialize()
        Window(
            onCloseRequest = ::exitApplication,
            title = TextResources.Title,
            state = rememberWindowState(width = WindowSetup.width.dp, height = WindowSetup.height.dp),
        ) {
            defineCurrentScreen(getState())
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