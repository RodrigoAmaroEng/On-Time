package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.utilities.withTag
import java.time.LocalDateTime


@Composable
fun CurrentTask(task: WorkingTask, onAction: (Actions) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth(Theme.Dimens.FULL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = MaterialTheme.colors.secondaryVariant, modifier = Modifier.weight(Theme.Dimens.FULL)) {
            Row(
                Modifier.height(Theme.Dimens.Height.XX_REGULAR).padding(Theme.Dimens.Margins.MEDIUM, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(task.task.id, style = MaterialTheme.typography.body1)
                    Text(task.task.title, style = MaterialTheme.extension.small)
                }
                Spacer(Modifier.weight(Theme.Dimens.FULL))
                Column {
                    ClockDisplay(task.startedAt, Icons.ELAPSED_TIME)
                    task.pomodoroStartedAt?.let {
                        ClockDisplay(it, Icons.POMODORO, withTag(Tags.PomodoroTimer))
                    }
                }

            }
        }
        SquareButton(
            Icons.POMODORO,
            size = ButtonSize.TOOLBAR,
            modifier = withTag(Tags.StartPomodoroButton),
            onClick = { onAction(Actions.StartPomodoro(task.task)) })
        SquareButton(
            Icons.STOP,
            size = ButtonSize.TOOLBAR,
            modifier = withTag(Tags.StopWorkingButton),
            onClick = { onAction(Actions.StopTask) })
    }
}

@Composable
@Preview
fun previewCurrentTask() {
    NewOnTimeTheme {
        Column {
            CurrentTask(
                WorkingTask(
                    Task("ABC-123", "Some task", TaskState.NOT_STARTED, false),
                    LocalDateTime.now(),
                    10
                ),
                {},
                Modifier
            )
            Spacer(Modifier.height(Theme.Dimens.Margins.MEDIUM))
            CurrentTask(
                WorkingTask(
                    Task("ABC-123", "Some task", TaskState.NOT_STARTED, false),
                    LocalDateTime.now().plusMinutes(-5),
                    10,
                    LocalDateTime.now().plusMinutes(-4),
                ),
                {},
                Modifier
            )
        }
    }
}