package dev.amaro.on_time.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState


@Composable
fun TaskUI(task: Task, onSelect: (Task) -> Unit = {}) {
    val showActionsState = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    Surface(Modifier.clickable { onSelect(task) }, color = MaterialTheme.colors.surface) {
        Box(
            Modifier.onHover(
                Portion(Alignment.CenterEnd, IntSize(30,100)),
                onHover = { isOver -> showActionsState.value = isOver }
            )
        ) {
            Row(
                Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(task.id, style = MaterialTheme.typography.subtitle1)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        task.title,
                        style = MaterialTheme.typography.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Image(painterResource(Icons.UNASSIGNED), "")
            }
            AnimatedVisibility(
                visible = showActionsState.value,
                enter = slideInHorizontally { with(density) { 32.dp.roundToPx() } },
                exit = slideOutHorizontally { with(density) { 32.dp.roundToPx() } },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Column {
                    SquareButton(
                        Icons.USER_ASSIGN,
                        ButtonState.HOVER,
                        size = ButtonSize.ACTIONS,
                    )
                    SquareButton(
                        Icons.USER_ASSIGN,
                        ButtonState.PRESSED,
                        size = ButtonSize.ACTIONS,
                    )
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
/**
Se Não tem assignação
- Assignar
- Iniciar
Se é minha
- Iniciar / Code Review / Ready to QA / Done
- Done
 **/