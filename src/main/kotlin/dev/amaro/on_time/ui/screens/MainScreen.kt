package dev.amaro.on_time.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Results
import dev.amaro.on_time.ui.*
import dev.amaro.on_time.utilities.withTag

@Composable
fun MainScreen(state: AppState, onAction: (Actions) -> Unit) =
    Screen(
        state,
        onAction,
        modifier = withTag(Tags.MainScreen),
        toolbarContent = {
            taskFilters(state, onAction)
        },
        content = {
            state.currentTask?.let {
                AnimatedVisibility(true, modifier = withTag(Tags.CurrentTask)) {
                    CurrentTask(it, { onAction(Actions.StopTask) })
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (state.configuration?.isValid == true) {
                    when (state.lastResult) {
                        Results.Idle -> {
                            if (state.tasks.isNotEmpty()) {
                                displayTasks(state, onAction)
                            } else {
                                Messages.noItemsToShow()
                            }
                        }
                        Results.NetworkError -> Messages.networkErrorMessage()
                        Results.Processing -> Messages.loading()
                    }
                } else {
                    Messages.noConfigurationMessage {
                        onAction(Actions.Navigation.GoToSettings)
                    }
                }
            }
        }
    )


@Composable
private fun taskFilters(
    state: AppState,
    onAction: (Actions) -> Unit
) {
    Row(withTag(Tags.TaskFilters)) {
        SquareButton(
            Icons.USER_ASSIGN,
            initialState = if (state.onlyMyTasks) ButtonState.CHECKED else ButtonState.NORMAL,
            onClick = { onAction(Actions.FilterMine) },
            modifier = withTag(Tags.FilterMineButton)
        )
    }
}

@Composable
private fun displayTasks(
    state: AppState,
    onAction: (Actions) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().testTag("QueryResults"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.tasks) {
            TaskUI(it,
                onSelect = { task -> onAction(Actions.StartTask(task)) },
                onTaskAction = { action -> onAction(action) })
        }
    }
}

object Messages {
    @Composable
    fun noConfigurationMessage(onAction: (Actions) -> Unit) {
        MessageBox(
            Icons.ON_QA,
            message = TextResources.Errors.NoConfiguration,
            actionText = TextResources.Actions.StartConfiguration,
            onAction = { onAction(Actions.Navigation.GoToSettings) }
        )
    }

    @Composable
    fun networkErrorMessage() {
        MessageBox(
            Icons.ON_QA,
            message = TextResources.Errors.NetworkError
        )
    }

    @Composable
    fun noItemsToShow() {
        MessageBox(
            Icons.ON_QA,
            message = TextResources.Errors.NoTasksAvailable
        )
    }

    @Composable
    fun loading() {
        MessageBox(
            Icons.ON_QA,
            message = TextResources.Loading
        )
    }
}