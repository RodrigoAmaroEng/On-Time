package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(
    actions: @Composable () -> Unit
) {
    Surface(Modifier.fillMaxWidth()) {
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
            SquareButton(Icons.USER_ASSIGN)
            SquareButton(Icons.TASK_DONE, initialState = ButtonState.CHECKED)
        }
    }
}

