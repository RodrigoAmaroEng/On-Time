package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskRow(task: Task, onSelect: (Task) -> Unit = {}) {
    Surface(
        Modifier.border(4.dp, MaterialTheme.extension.blackDark, shape = RoundedCornerShape(16.dp))
            .testTag("Task-${task.id}"),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            Modifier
                .padding(Theme.Dimens.Spacing.LARGE)
                .onClick { onSelect(task) }
                .fillMaxWidth()
        ) {
            Image(painterResource(task.status.icon), "", Modifier.height(80.dp).padding(Theme.Dimens.Spacing.MEDIUM))
            Spacer(Modifier.width(Theme.Dimens.Spacing.LARGE))
            Column {
                Text(task.id, style = Theme.TextStyleDefinitions.Title)
                Text(task.title, style = Theme.TextStyleDefinitions.Body)
            }
        }
    }
}



@Preview
@Composable
fun previewNewTask() {
    val task = Task("CST-504", "Flex Currency for OfferHub is a very long task title", TaskState.NOT_STARTED)
    NewOnTimeTheme {
        Column {
            Box(Modifier.padding(8.dp)) {
                TaskRow(task)
            }
            Box(Modifier.padding(8.dp)) {
                TaskRow(task.copy(status = TaskState.WORKING))
            }
            Box(Modifier.padding(8.dp)) {
                TaskRow(task.copy(status = TaskState.ON_REVIEW))
            }
            Box(Modifier.padding(8.dp)) {
                TaskRow(task.copy(status = TaskState.ON_QA))
            }
        }
    }
}
