package dev.amaro.on_time.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.*
import dev.amaro.on_time.utilities.withTag

@Composable
fun MainScreen(state: AppState, onAction: (Actions) -> Unit) =
    OnTimeTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = withTag("MainScreen")) {
                Toolbar {
                    SquareButton(
                        Icons.USER_ASSIGN,
                        initialState = if (state.onlyMyTasks) ButtonState.CHECKED else ButtonState.NORMAL,
                        onClick = { onAction(Actions.FilterMine) },
                        modifier = withTag("OnlyMyTasks")
                    )
                }
                state.currentTask?.let {
                    AnimatedVisibility(true) {
                        CurrentTask(it, withTag("CurrentTask"))
                    }
                }
                if (state.configuration != null) {
                    LazyColumn(
                        Modifier.fillMaxSize().testTag("QueryResults"), verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.tasks) {
                            TaskUI(it,
                                onSelect = { task -> onAction(Actions.StartTask(task)) },
                                onTaskAction = { action -> onAction(action) })
                        }
                    }
                } else {
                    MessageBox(
                        Icons.ON_QA,
                        message = "No configuration was found",
                        modifier = Modifier.testTag("StartConfigurationButton")
                    )
                }
            }
        }
    }
