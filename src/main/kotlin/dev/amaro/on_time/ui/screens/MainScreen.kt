package dev.amaro.on_time.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Results
import dev.amaro.on_time.ui.*
import dev.amaro.on_time.utilities.Constants
import dev.amaro.on_time.utilities.withTag
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import java.time.LocalDateTime

@Composable
fun MainScreen(state: AppState, onAction: (Actions) -> Unit) =
    Screen(
        state,
        onAction,
        modifier = withTag(Tags.MainScreen),
        toolbarContent = {
            SquareButton(
                Icons.Toolbar.REFRESH,
                modifier = withTag(Tags.RefreshButton),
            ) { onAction(Actions.Refresh) }
            taskFilters(state, onAction)
            state.lastTask?.let {
                SquareButton(
                    Icons.Toolbar.RESUME,
                    modifier = withTag(Tags.ResumeTaskButton)
                ) { onAction(Actions.StartTask(it)) }
            }

        },
        content = {
            val searchFlow = remember { mutableStateOf("") }

            LaunchedEffect(searchFlow) {
                snapshotFlow { searchFlow.value }
                    .distinctUntilChanged()
                    .debounce(500)
                    .filter { it.length > 2 || it.isEmpty() }
                    .collect {
                        onAction(Actions.Search(it))
                    }
            }

            Surface(color = MaterialTheme.colors.primary) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(Theme.Dimens.Height.REGULAR)
                ) {
                    Text(
                        "Search:",
                        style = MaterialTheme.extension.section,
                        modifier = Modifier.padding(Theme.Dimens.Spacing.LARGE, 0.dp)
                    )
                    BasicTextField(
                        value = searchFlow.value,
                        onValueChange = { searchFlow.value = it },
                        textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.extension.whiteDark),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                if (state.configuration?.isValid == true) {
                    when (state.lastResult) {
                        Results.Idle -> {
                            if (state.tasks.isNotEmpty()) {
                                displayTasks(state, onAction)
                            } else {
                                Messages.noItemsToShow()
                            }
                        }
                        is Results.Errors.ServiceError -> Messages.serviceError(state.lastResult.message)
                        Results.Errors.NetworkError -> Messages.networkErrorMessage()
                        Results.Processing -> Messages.loading()
                    }
                } else {
                    Messages.noConfigurationMessage {
                        onAction(Actions.Navigation.GoToSettings)
                    }
                }
            }
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
        Image(painterResource(Icons.BREAK), Constants.EMPTY, modifier = Modifier.size(Theme.Dimens.Icons.SMALL))
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
@Preview
fun previewMainScreen() {
    NewOnTimeTheme {
        MainScreen(AppState()) { }
    }
}

@Composable
private fun taskFilters(
    state: AppState,
    onAction: (Actions) -> Unit
) {
    Row(withTag(Tags.TaskFilters)) {
        SquareButton(
            Icons.Toolbar.MY_TASKS,
            initialState = if (state.onlyMyTasks) ButtonState.CHECKED else ButtonState.NORMAL,
            modifier = withTag(Tags.FilterMineButton)
        ) { onAction(Actions.FilterMine) }
    }
}

@Composable
private fun displayTasks(
    state: AppState,
    onAction: (Actions) -> Unit
) {
    LazyColumn(
        Modifier.padding(Theme.Dimens.Spacing.LARGE)
            .fillMaxSize()
            .testTag(Tags.QueryResults),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.tasks) {
            TaskRow(
                it,
                onSelect = { task -> onAction(Actions.StartTask(task)) },
            )
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
    fun serviceError(serviceErrorMessage: String) {
        MessageBox(
            Icons.ON_QA,
            message = serviceErrorMessage
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
