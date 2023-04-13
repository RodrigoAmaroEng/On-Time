package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(
    actions: @Composable () -> Unit
) {
    Surface(color = MaterialTheme.extension.blackDark, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(Theme.Dimens.Spacing.MEDIUM, 0.dp)) {
            actions()
        }
    }
}

@Preview
@Composable
fun previewToolbar() {
    NewOnTimeTheme {
        Toolbar {
            SquareButton(Icons.Toolbar.SAVE)
            SquareButton(Icons.POMODORO, initialState = ButtonState.CHECKED)
        }
    }
}

