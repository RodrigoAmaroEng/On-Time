package dev.amaro.on_time

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import dev.amaro.on_time.ui.screens.MainScreen


fun main() = application {
    val app = remember { mutableStateOf(OnTimeApp()) }
    app.value.initialize()
    MainScreen(app.value, ::exitApplication)
}