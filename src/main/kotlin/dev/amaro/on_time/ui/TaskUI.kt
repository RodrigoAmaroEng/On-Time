package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState

@Composable
fun TaskUI(task : Task) {
    Row(Modifier.padding(12.dp)) {
        Column {
            Text(task.id)
            Text(task.title)
        }
    }
}

@Preview
@Composable
fun previewTask() {
    TaskUI(Task("CST-504", "Flex Currency for OfferHub", TaskState.UNASSIGNED))
}