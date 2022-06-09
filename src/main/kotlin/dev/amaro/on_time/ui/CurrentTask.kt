package dev.amaro.on_time.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.WorkingTask


@Composable
fun CurrentTask(task: WorkingTask) {
    Surface(color = MaterialTheme.colors.primary) {
        Column(Modifier.padding(12.dp).fillMaxWidth(1f)) {
            Row {
                Text(task.task.id)
                Spacer(Modifier.weight(1f))
                ClockDisplay(task.startedAt)
            }
        }
    }
}