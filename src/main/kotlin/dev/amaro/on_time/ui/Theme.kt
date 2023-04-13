package dev.amaro.on_time.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.*

@Immutable
data class ThemeExtension(
    val title: TextStyle,
    val section: TextStyle,
    val small: TextStyle,
    val primaryLight: Color,
    val secondaryLight: Color,
    val blackBase: Color,
    val blackLight: Color,
    val blackDark: Color,
    val blackOver: Color,
    val blackOverPrimary: Color,
    val blackOverSecondary: Color,
    val whiteBase: Color,
    val whiteLight: Color,
    val whiteDark: Color,
    val whiteOver: Color,
    val whiteOverPrimary: Color,
    val redDark: Color,
    val redLight: Color,
    val redOver: Color,
    val greenDark: Color,
    val greenLight: Color,
    val greenOver: Color,

    )
val LocalThemeExtension = staticCompositionLocalOf {
    ThemeExtension(
        title = TextStyle.Default,
        section = TextStyle.Default,
        small = TextStyle.Default,
        primaryLight = Color.Unspecified,
        secondaryLight = Color.Unspecified,
        blackBase = Color.Unspecified,
        blackLight = Color.Unspecified,
        blackDark = Color.Unspecified,
        blackOver = Color.Unspecified,
        blackOverPrimary = Color.Unspecified,
        blackOverSecondary = Color.Unspecified,
        whiteBase = Color.Unspecified,
        whiteLight = Color.Unspecified,
        whiteDark = Color.Unspecified,
        whiteOver = Color.Unspecified,
        whiteOverPrimary = Color.Unspecified,
        redDark = Color.Unspecified,
        redLight = Color.Unspecified,
        redOver = Color.Unspecified,
        greenDark = Color.Unspecified,
        greenLight = Color.Unspecified,
        greenOver = Color.Unspecified,
    )
}

@Composable
fun NewOnTimeTheme(block: @Composable () -> Unit) {
    val themeExtension = ThemeExtension(
        title = Theme.TextStyleDefinitions.Title,
        section = Theme.TextStyleDefinitions.Section,
        small = Theme.TextStyleDefinitions.Small,
        primaryLight = Theme.Colors.Primary.light,
        secondaryLight = Theme.Colors.Secondary.light,
        blackBase = Theme.Colors.Black.base,
        blackLight = Theme.Colors.Black.light,
        blackDark = Theme.Colors.Black.dark,
        blackOver = Theme.Colors.Black.Over.regular,
        blackOverPrimary = Theme.Colors.Black.Over.primary,
        blackOverSecondary = Theme.Colors.Black.Over.secondary,
        whiteBase = Theme.Colors.White.base,
        whiteLight = Theme.Colors.White.light,
        whiteDark = Theme.Colors.White.dark,
        whiteOver = Theme.Colors.White.Over.regular,
        whiteOverPrimary = Theme.Colors.White.Over.primary,
        redDark = Theme.Colors.Red.dark,
        redLight = Theme.Colors.Red.light,
        redOver = Theme.Colors.Red.over,
        greenDark = Theme.Colors.Green.dark,
        greenLight = Theme.Colors.Green.light,
        greenOver = Theme.Colors.Green.over,
    )
    CompositionLocalProvider(LocalThemeExtension provides themeExtension) {
        MaterialTheme(
            typography = MaterialTheme.typography.copy(
                h1 = Theme.TextStyleDefinitions.H1,
                h2 = Theme.TextStyleDefinitions.H2,
                h3 = Theme.TextStyleDefinitions.H3,
                subtitle1 = Theme.TextStyleDefinitions.Title
            ),
            colors = MaterialTheme.colors.copy(
                primary = Theme.Colors.Primary.base,
                primaryVariant = Theme.Colors.Primary.dark,
                onPrimary = Theme.Colors.Primary.over,
                secondary = Theme.Colors.Secondary.base,
                secondaryVariant = Theme.Colors.Secondary.dark,
                onSecondary = Theme.Colors.Secondary.over,

                surface = Theme.Colors.Black.light,
                onSurface = Theme.Colors.Black.Over.regular,

                background = Theme.Colors.Black.base,
                onBackground = Theme.Colors.Black.Over.regular,
                error = Theme.Colors.Red.dark,
                onError = Theme.Colors.Red.over,

                )
        ) {
            block()
        }
    }
}

val MaterialTheme.extension: ThemeExtension
    @Composable
    get() = LocalThemeExtension.current

object Theme  {
    object Colors {
        object Primary {
            val light = Color(0xFF81D4FA)
            val base = Color(0xFF03A9F4)
            val dark = Color(0xFF01579B)
            val over = Color(0xFFECEFF1)
        }
        object Secondary {
            val light = Color(0xFFFFCC80)
            val base = Color(0xFFFF9800)
            val dark = Color(0xFFEF6C00)
            val over = Color(0xFFECEFF1)
        }
        object Black {
            val light = Color(0xFF404040)
            val base = Color(0xFF2B2B2B)
            val dark = Color(0xFF1C1A1A)
            object Over {
                val regular = Color(0xFFECEFF1)
                val primary = Color(0xFF81D4FA)
                val secondary = Color(0xFFFFCC80)
            }
        }
        object White {
            val light = Color(0xFFFFFFFF)
            val base = Color(0xFFF0F0F0)
            val dark = Color(0xFFE0E0E0)
            object Over {
                val regular = Color(0xFF404040)
                val primary = Color(0xFF01579B)
                val secondary = Color(0xFFEF6C00)
            }
        }
        object Red {
            val light =  Color(0xFFCA494D)
            val dark =  Color(0xFFA42327)
            val over = Color(0xFFFFFFFF)
        }
        object Green {
            val light =  Color(0xFF85E0BC)
            val dark =  Color(0xFF25D08C)
            val over = Color(0xFF1C1A1A)
        }

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
    object Fonts {
        val main  = FontFamily(
            Font(
                resource = "fonts/Exo2/Exo2-Bold.ttf",
                weight = FontWeight.Bold,
                style = FontStyle.Normal
            ),
            Font(
                resource = "fonts/Exo2/Exo2-Medium.ttf",
                weight = FontWeight.Medium,
                style = FontStyle.Normal
            ),
            Font(
                resource = "fonts/Exo2/Exo2-Regular.ttf",
                weight = FontWeight.Normal,
                style = FontStyle.Normal
            ),
        )
    }
    @OptIn(ExperimentalUnitApi::class)
    object TextStyleDefinitions {
        val H1 = TextStyle.Default.copy(
            fontFamily = Fonts.main,
            fontSize = 48.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = TextUnit(0.05f, TextUnitType.Em),
            lineHeight = TextUnit(1.10f, TextUnitType.Em)
        )
        val H2 = TextStyle.Default.copy(
            fontFamily = Fonts.main,
            fontSize = 39.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = TextUnit(0.05f, TextUnitType.Em),
            lineHeight = TextUnit(1.10f, TextUnitType.Em)
        )
        val H3 = TextStyle.Default.copy(
            fontFamily = Fonts.main,
            fontSize = 31.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = TextUnit(0.05f, TextUnitType.Em),
            lineHeight = TextUnit(1.10f, TextUnitType.Em)
        )
        val Title = TextStyle.Default.copy(
            fontFamily = Fonts.main,
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = TextUnit(0.02f, TextUnitType.Em),
            lineHeight = TextUnit(1.20f, TextUnitType.Em)
        )
        val Section = TextStyle.Default.copy(
            fontFamily = Fonts.main,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = TextUnit(0.02f, TextUnitType.Em),
            lineHeight = TextUnit(1.30f, TextUnitType.Em)
        )
        val Body = TextStyle.Default.copy(
            fontFamily = Fonts.main,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = TextUnit(0.02f, TextUnitType.Em),
            lineHeight = TextUnit(1.50f, TextUnitType.Em)
        )
        val Small = TextStyle.Default.copy(
            fontFamily = Fonts.main,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = TextUnit(0.05f, TextUnitType.Em),
            lineHeight = TextUnit(1.50f, TextUnitType.Em)
        )

    }
}
