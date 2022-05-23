package dev.amaro.on_time.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OnTimeTheme(block: @Composable () -> Unit) {
    MaterialTheme(
        typography = MaterialTheme.typography.copy(
            subtitle1 = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold
            )
        ),
        colors = MaterialTheme.colors.copy(
            surface = OnTimeColors.surfaceColor,
            background = OnTimeColors.backgroundColor
        )
    ) {
        block()
    }
}
object OnTimeColors {
    val surfaceColor = Color(0xFFD9D9D9)
    val backgroundColor =  Color(0xFFF0F0F0)
}