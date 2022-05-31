package dev.amaro.on_time.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.Task
import java.time.LocalDateTime


@Composable
fun CurrentTask(task: Task) {
    Surface(color = MaterialTheme.colors.primaryVariant) {
        Column(Modifier.padding(12.dp).fillMaxWidth(1f)) {
            Row {
                Text(task.id)
                Spacer(Modifier.weight(1f))
                ClockDisplay(LocalDateTime.now())
            }
        }
    }
}