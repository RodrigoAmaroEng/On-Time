package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.amaro.on_time.utilities.Constants


@Composable
fun SquareButton(
    icon: String,
    initialState: ButtonState = ButtonState.NORMAL,
    size: ButtonSize = ButtonSize.TOOLBAR,
    modifier: Modifier = Modifier,
    text: String? = null,
    onClick: () -> Unit = {}
) {
    val state = remember { mutableStateOf(initialState) }

    Surface(color = getBackgroundColorForState(state.value), modifier = modifier) {
        Box(
            Modifier
                .size(size.size)
                .onStateChange {
                    state.value =
                        if (initialState != ButtonState.CHECKED || it == ButtonState.HOVER) it else ButtonState.CHECKED
                }
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        )
        {
            Image(
                painterResource(icon),
                Constants.EMPTY,
                colorFilter = ColorFilter.tint(getForegroundColorForState(state.value)),
                modifier = Modifier.size(size.iconSize)
            )
            if (text != null && size != ButtonSize.ACTIONS) {
                Text(
                    text,
                    style = MaterialTheme.typography.caption.copy(fontSize = size.fontSize, fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface.copy(alpha = 0.7f))
                        .padding(0.dp, Theme.Dimens.Spacing.TINY)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.onStateChange(stateHandler: (ButtonState) -> Unit) =
    onPointerEvent(
        eventType = PointerEventType.Press,
        onEvent = { stateHandler(ButtonState.PRESSED) }
    )
        .onPointerEvent(
            eventType = PointerEventType.Enter,
            onEvent = { stateHandler(ButtonState.HOVER) }
        )
        .onPointerEvent(
            eventType = PointerEventType.Exit,
            onEvent = { stateHandler(ButtonState.NORMAL) }
        )

enum class ButtonState {
    NORMAL,
    HOVER,
    PRESSED,
    CHECKED
}

enum class ButtonSize(val size: Dp, val iconSize: Dp, val fontSize: TextUnit) {
    TOOLBAR(Theme.Dimens.Icons.EXTRA_LARGE, Theme.Dimens.Icons.LARGE, Theme.Dimens.Fonts.SMALL),
    REGULAR(Theme.Dimens.Icons.LARGE, Theme.Dimens.Icons.MEDIUM, Theme.Dimens.Fonts.TINY),
    ACTIONS(Theme.Dimens.Icons.MEDIUM, Theme.Dimens.Icons.SMALL, 0.sp)
}

@Composable
private fun getBackgroundColorForState(state: ButtonState) = when (state) {
    ButtonState.NORMAL -> Theme.Colors.transparent
    ButtonState.HOVER -> MaterialTheme.colors.secondaryVariant
    ButtonState.CHECKED,
    ButtonState.PRESSED -> MaterialTheme.colors.surface
}

@Composable
private fun getForegroundColorForState(state: ButtonState) = when (state) {
    ButtonState.NORMAL -> MaterialTheme.colors.background
    ButtonState.HOVER -> MaterialTheme.colors.primaryVariant
    ButtonState.CHECKED,
    ButtonState.PRESSED -> MaterialTheme.colors.primary
}

@Preview
@Composable
fun previewButton() {
    OnTimeTheme {
        Column(Modifier.background(MaterialTheme.colors.secondary)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "STATUS",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(100.dp)
                )
                ButtonSize.values().forEach { size ->
                    listOf("NT", "T").forEach { text->
                        Spacer(Modifier.width(Theme.Dimens.Spacing.SMALL).background(MaterialTheme.colors.primary))
                        Text(
                            text = text,
                            style = MaterialTheme.typography.caption,
                            color = MaterialTheme.colors.onSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(size.size)
                        )
                    }
                }
            }
            ButtonState.values().forEach { state ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.name,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(100.dp)
                    )

                    ButtonSize.values().forEach { size ->
                        listOf<String?>(null, "CAT-1234").forEach { text->
                            Spacer(Modifier.width(Theme.Dimens.Spacing.SMALL).background(MaterialTheme.colors.primary))
                            SquareButton(Icons.USER_ASSIGN, state, size, text = text)
                        }
                    }
                }
            }

        }
    }
}