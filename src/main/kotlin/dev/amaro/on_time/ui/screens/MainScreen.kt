package dev.amaro.on_time.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import dev.amaro.on_time.OnTimeApp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.ui.*

@Composable
fun MainScreen(app: OnTimeApp, onExit : () -> Unit) = Window(
    onCloseRequest = onExit,
    title = "On Time - Task Manager",
    state = rememberWindowState(width = 500.dp, height = 300.dp),
) {
    app.getState().apply {
        OnTimeTheme {
            Surface(color = MaterialTheme.colors.background) {
                Column {
                    Toolbar(
                        ButtonDef(Icons.USER_ASSIGN, onlyMyTasks) { app.perform(Actions.FilterMine) },
                    )
                    currentTask?.let {
                        AnimatedVisibility(true) { CurrentTask(it) }
                    }
                    LazyColumn(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tasks) {
                            TaskUI(it) { task ->
                                app.perform(Actions.StartTask(task))
                            }
                        }
                    }
                }
            }
        }
    }
}