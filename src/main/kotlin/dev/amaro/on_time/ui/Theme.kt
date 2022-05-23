package dev.amaro.on_time.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Theme(block: @Composable () -> Unit) {
    MaterialTheme(
        typography = MaterialTheme.typography.copy(
            subtitle1 = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold
            )
        ),
        colors = MaterialTheme.colors.copy(
            surface = Color(0xD9D9D9),
            background = Color(0xF0F0F0)
        )
    ) {
        block()
    }
}