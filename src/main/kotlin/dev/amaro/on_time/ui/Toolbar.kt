package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(vararg buttons: ButtonDef) {
    Surface(Modifier.fillMaxWidth().height(48.dp), color = MaterialTheme.colors.secondary) {
        Row {
            buttons.forEach {
                it.apply {
                    SquareButton(
                        imageResource,
                        if (checked) ButtonState.CHECKED else ButtonState.NORMAL,
                        onClick = onClick,
                    )
                }
            }
        }
    }
}


data class ButtonDef(
    val imageResource: String,
    val checked: Boolean = false,
    val onClick: () -> Unit = {}
)

@Preview
@Composable
fun previewToolbar() {
    OnTimeTheme {
        Toolbar(
            ButtonDef(Icons.USER_ASSIGN),
            ButtonDef(Icons.TASK_DONE, checked = true)
        )
    }
}

