package dev.amaro.on_time.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState


@Composable
fun TaskUI(task: Task, onSelect: (Task) -> Unit = {}, onTaskAction: (Actions) -> Unit = {}) {
    val showActionsState = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    Surface(
        modifier = Modifier.clickable { onSelect(task) }.testTag("Task-${task.id}"),
        color = MaterialTheme.colors.surface
    ) {
        Box(
            Modifier.onHover(
                Portion(Alignment.CenterEnd, IntSize(30, 100)),
                onHover = { isOver -> showActionsState.value = isOver }
            )
        ) {
            Row(
                Modifier.padding(Theme.Dimens.Margins.MEDIUM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(task.status.icon), task.status.name)
                Spacer(Modifier.width(Theme.Dimens.Spacing.LARGE))
                Column(Modifier.weight(Theme.Dimens.FULL)) {
                    Text(task.id, style = MaterialTheme.typography.subtitle1)
                    Spacer(Modifier.height(Theme.Dimens.Spacing.SMALL))
                    Text(
                        task.title,
                        style = MaterialTheme.typography.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            AnimatedVisibility(
                visible = showActionsState.value,
                enter = slideInHorizontally { with(density) { 32.dp.roundToPx() } },
                exit = slideOutHorizontally { with(density) { 32.dp.roundToPx() } },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Column(Modifier.background(MaterialTheme.colors.secondary)) {
                    task.actionsAvailable.forEach {
                        SquareButton(
                            it.icon,
                            size = ButtonSize.ACTIONS,
                            onClick = { onTaskAction(it.action) }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun previewTask() {
    OnTimeTheme {
        TaskUI(Task("CST-504", "Flex Currency for OfferHub is a very long task title", TaskState.NOT_STARTED))
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

/**
Se Não tem assignação
- Assignar
- Iniciar
Se é minha
- Iniciar / Code Review / Ready to QA / Done
- Done
 **/

