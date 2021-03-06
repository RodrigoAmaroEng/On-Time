package dev.amaro.on_time

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Navigation
import dev.amaro.on_time.ui.screens.MainScreen
import dev.amaro.on_time.ui.screens.SettingsScreen


fun main() = application {
    val app = remember { mutableStateOf(OnTimeApp(AppState(), Modules.release)) }
    app.value.apply {
        initialize()
        Window(
            onCloseRequest = ::exitApplication,
            title = "On Time - Task Manager",
            state = rememberWindowState(width = 500.dp, height = 300.dp),
        ) {
            val state = getState()
            if (state.screen == Navigation.Main) {
                MainScreen(state, { perform(it) })
            } else if (state.screen == Navigation.Configuration) {
                SettingsScreen(state, { perform(it) })
            }
        }
    }
}