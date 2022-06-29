package dev.amaro.on_time.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.OnTimeTheme
import dev.amaro.on_time.utilities.withTag

@Composable
fun SettingsScreen(state: AppState, onAction: (Actions) -> Unit) = OnTimeTheme {
    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = withTag("MainScreen")) {
        }
    }
}