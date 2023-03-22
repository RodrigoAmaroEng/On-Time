package dev.amaro.on_time.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Results
import dev.amaro.on_time.ui.*
import dev.amaro.on_time.utilities.withTag
import java.time.LocalDateTime

@Composable
fun MainScreen(state: AppState, onAction: (Actions) -> Unit) =
    Screen(
        state,
        onAction,
        modifier = withTag(Tags.MainScreen),
        toolbarContent = {
            SquareButton(
                Icons.REFRESH,
                onClick = { onAction(Actions.Refresh) },
                modifier = withTag(Tags.RefreshButton),
            )
            taskFilters(state, onAction)
            state.lastTask?.let {
                SquareButton(
                    Icons.RESUME,
                    onClick = { onAction(Actions.StartTask(it)) },
                    modifier = withTag(Tags.ResumeTaskButton),
                    text = it.id
                )
            }

        },
        content = {
            state.currentTask?.let {
                AnimatedVisibility(true, modifier = withTag(Tags.CurrentTask)) {
                    CurrentTask(it, { onAction(it) })
                }
            }
            state.breakStartedAt?.let {
                AnimatedVisibility(true, modifier = withTag(Tags.BreakTimer)) {
                    BreakAlert(it)
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
private fun BreakAlert(startedAt: LocalDateTime) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(Theme.Colors.lightGreen)
            .padding(Theme.Dimens.Margins.MEDIUM, Theme.Dimens.Margins.SMALL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painterResource(Icons.BREAK), "Break", modifier = Modifier.size(Theme.Dimens.Icons.SMALL))
        Spacer(modifier = Modifier.width(Theme.Dimens.Margins.SMALL))
        Text(TextResources.Informative.TakeABreak)
        Spacer(modifier = Modifier.weight(1f))
        ClockDisplay(startedAt)
    }
}

@Composable
@Preview
fun previewBreakAlert() {
    BreakAlert(LocalDateTime.now().minusMinutes(2))
}

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
        Modifier.fillMaxSize().testTag(Tags.QueryResults),
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