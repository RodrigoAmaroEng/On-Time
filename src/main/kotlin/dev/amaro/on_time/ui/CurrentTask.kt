package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.utilities.withTag
import java.time.LocalDateTime


@Composable
fun CurrentTask(task: WorkingTask, onStop: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(1f)
            .background(MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = MaterialTheme.colors.primary, modifier = Modifier.weight(1f)) {
            Row(Modifier.padding(12.dp)) {
                Text(task.task.id)
                Spacer(Modifier.weight(1f))
                ClockDisplay(task.startedAt)
            }
        }
        SquareButton(
            Icons.TASK_DONE,
            size = ButtonSize.ACTIONS,
            modifier = withTag("StopWorkingButton"),
            onClick = { onStop() })
    }
}

@Composable
@Preview
fun previewCurrentTask() {
    OnTimeTheme {
        CurrentTask(
            WorkingTask(
                Task("ABC-123", "Some task", TaskState.NOT_STARTED, false),
                LocalDateTime.now(),
                10
            ),
            {}
        )
    }
}