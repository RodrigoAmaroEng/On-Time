package dev.amaro.on_time.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.Task


@Composable
fun CurrentTask(task: Task) {
    Surface(color = MaterialTheme.colors.primaryVariant) {
        Column(Modifier.padding(12.dp).fillMaxWidth(1f)) {
            Row() {
                Text(task.id)
            }
        }
    }
}