package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.utilities.withTag
import java.time.LocalDateTime


@Composable
fun CurrentTask(task: WorkingTask, onStop: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(Theme.Dimens.FULL)
            .background(MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = MaterialTheme.colors.primary, modifier = Modifier.weight(Theme.Dimens.FULL)) {
            Row(Modifier.padding(Theme.Dimens.Margins.MEDIUM)) {
                Text(task.task.id)
                Spacer(Modifier.weight(Theme.Dimens.FULL))
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