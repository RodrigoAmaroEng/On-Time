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
            primaryVariant = OnTimeColors.primaryVariantColor,
            onSurface = OnTimeColors.onSurfaceColor,
            background = OnTimeColors.backgroundColor
        )
    ) {
        block()
    }
}
object OnTimeColors {
    val surfaceColor = DeclaredColors.darkGray
    val onSurfaceColor = DeclaredColors.lightBlack
    val backgroundColor =  DeclaredColors.lightGray
    val primaryVariantColor = DeclaredColors.lightBlue
}

object DeclaredColors {
    val lightBlue = Color(0xFF9AC4F8)
    val darkBlue =  Color(0xFF4090F2)

    val lightGreen = Color(0xFF99EDCC)
    val darkGreen =  Color(0xFF25D08C)

    val lightRed = Color(0xFFE2787C)
    val darkRed =  Color(0xFFC1292E)

    val lightGray = Color(0xFFEEEEEE)
    val darkGray =  Color(0xFFD6D6D6)

    val lightBlack = Color(0xFF474747)
    val darkBlack =  Color(0xFF333333)
}