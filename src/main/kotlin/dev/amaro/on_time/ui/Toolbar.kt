package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(body: @Composable() () -> Unit) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Column {
            Row(Modifier.height(48.dp)) {
                body()
            }
            Box(Modifier.height(2.dp).fillMaxWidth().background(MaterialTheme.colors.primary))
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

