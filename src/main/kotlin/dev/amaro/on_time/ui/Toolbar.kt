package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Toolbar(
    actions: @Composable () -> Unit,
    navigation: @Composable () -> Unit
) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Row {
            actions()
            Spacer(Modifier.weight(1f))
            navigation()
        }
    }
}

@Preview
@Composable
fun previewToolbar() {
    OnTimeTheme {
        Toolbar(
            {
                SquareButton(Icons.USER_ASSIGN)
                SquareButton(Icons.TASK_DONE, initialState = ButtonState.CHECKED)
            },
            {
                SquareButton(Icons.SETTINGS)
            }
        )
    }
}

