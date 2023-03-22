package dev.amaro.on_time.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnTimeTheme(block: @Composable () -> Unit) {
    MaterialTheme(
        typography = MaterialTheme.typography.copy(
            subtitle1 = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.Bold
            )
        ),
        colors = MaterialTheme.colors.copy(
            surface = Theme.Colors.darkGray,
            onSurface = Theme.Colors.lightBlack,
            primary = Theme.Colors.darkBlue,
            primaryVariant = Theme.Colors.lightBlue,
            background = Theme.Colors.lightGray,
            onBackground = Theme.Colors.lightBlack,
            secondary = Theme.Colors.darkBlack,
            secondaryVariant = Theme.Colors.lightBlack,
            onSecondary = Theme.Colors.lightGray
        )
    ) {
        block()
    }
}

object Theme  {
    object Colors {
        val lightBlue = Color(0xFF9AC4F8)
        val darkBlue = Color(0xFF4090F2)

        val lightGreen = Color(0xFF99EDCC)
        val darkGreen = Color(0xFF25D08C)

        val lightRed = Color(0xFFE2787C)
        val darkRed = Color(0xFFC1292E)

        val lightGray = Color(0xFFEEEEEE)
        val darkGray = Color(0xFFD6D6D6)

        val lightBlack = Color(0xFF474747)
        val darkBlack = Color(0xFF333333)

        val transparent = Color(0x00000000)
    }
    object Dimens {
        object Icons {
            val TINY = 16.dp
            val SMALL = 24.dp
            val MEDIUM = 32.dp
            val LARGE = 48.dp
            val EXTRA_LARGE = 64.dp
        }
        object Margins {
            val SMALL = 8.dp
            val MEDIUM = 12.dp
            val LARGE = 20.dp
        }
        object Spacing {
            val TINY = 2.dp
            val SMALL = 4.dp
            val MEDIUM = 8.dp
            val LARGE = 16.dp
        }
        object Height {
            val SMALL = 32.dp
            val REGULAR = 48.dp
            val X_REGULAR = 56.dp
            val XX_REGULAR = 64.dp
        }
        object Fonts {
            val TINY = 9.sp
            val SMALL = 12.sp
            val MEDIUM = 14.sp
            val LARGE = 16.sp
            val EXTRA_LARGE = 20.sp
        }
        const val FULL = 1f
        val BORDER = 1.dp
    }
}
