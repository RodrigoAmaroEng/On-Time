package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
                .size(size.size.dp)
                .padding(8.dp)
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
                modifier = Modifier.fillMaxSize()
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

enum class ButtonSize(val size: Int) {
    TOOLBAR(48),
    ACTIONS(32)
}

@Composable
private fun getBackgroundColorForState(state: ButtonState) = when (state) {
    ButtonState.NORMAL -> DeclaredColors.transparent
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
            SquareButton(Icons.TASK_DONE, ButtonState.CHECKED)
            SquareButton(Icons.USER_ASSIGN)
            SquareButton(Icons.TASK_DONE, ButtonState.HOVER)
            SquareButton(Icons.USER_ASSIGN, ButtonState.PRESSED)
        }
    }
}