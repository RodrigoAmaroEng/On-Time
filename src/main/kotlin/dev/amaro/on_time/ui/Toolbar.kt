package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Toolbar(body: @Composable() () -> Unit) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Column {
            Row(Modifier.height(Theme.Dimens.Height.REGULAR)) {
                body()
            }
        }
    }
}

@Preview
@Composable
fun previewToolbar() {
    OnTimeTheme {
        Toolbar {
            SquareButton(Icons.USER_ASSIGN)
            SquareButton(Icons.TASK_DONE, initialState = ButtonState.CHECKED)
        }
    }
}

