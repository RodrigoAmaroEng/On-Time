package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState

@Composable
fun TaskUI(task: Task, onSelect: (Task) -> Unit = {}) {
    Surface(Modifier.clickable { onSelect(task) }, color = OnTimeColors.surfaceColor) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(task.id, style = MaterialTheme.typography.subtitle1)
                Spacer(Modifier.height(4.dp))
                Text(task.title, style = MaterialTheme.typography.body2, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Image(painterResource("images/ic_unassigned.svg"), "")
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