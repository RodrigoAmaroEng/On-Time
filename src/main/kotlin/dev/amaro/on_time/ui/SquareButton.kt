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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.utilities.Constants


@Composable
fun SquareButton(
    icon: String,
    initialState: ButtonState = ButtonState.NORMAL,
    size: ButtonSize = ButtonSize.TOOLBAR,
    modifier: Modifier = Modifier,
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

enum class ButtonSize(val size: Dp, val iconSize: Dp) {
    TOOLBAR(Theme.Dimens.Icons.EXTRA_LARGE, Theme.Dimens.Icons.LARGE),
    REGULAR(Theme.Dimens.Icons.LARGE, Theme.Dimens.Icons.MEDIUM),
    ACTIONS(Theme.Dimens.Icons.MEDIUM, Theme.Dimens.Icons.SMALL)
}

@Composable
private fun getBackgroundColorForState(state: ButtonState) = when (state) {
    ButtonState.NORMAL -> MaterialTheme.extension.blackDark
    ButtonState.HOVER -> MaterialTheme.extension.blackBase
    ButtonState.CHECKED,
    ButtonState.PRESSED -> MaterialTheme.extension.blackLight
}

@Composable
private fun getForegroundColorForState(state: ButtonState) = when (state) {
    ButtonState.NORMAL -> MaterialTheme.colors.onSurface
    ButtonState.HOVER -> MaterialTheme.extension.blackOver
    ButtonState.CHECKED,
    ButtonState.PRESSED -> MaterialTheme.extension.blackOverPrimary
}

@Preview
@Composable
fun previewButton() {
    NewOnTimeTheme {
        Column(Modifier.background(MaterialTheme.extension.blackDark)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "STATUS",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(100.dp)
                )
                ButtonSize.values().forEach { size ->
                    listOf("NT", "T").forEach { text ->
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
                        listOf(null, "CAT-1234").forEach { text ->
                            Spacer(Modifier.width(Theme.Dimens.Spacing.SMALL).background(MaterialTheme.colors.primary))
                            SquareButton(Icons.Toolbar.SAVE, state, size)
                        }
                    }
                }
            }

        }
    }
}