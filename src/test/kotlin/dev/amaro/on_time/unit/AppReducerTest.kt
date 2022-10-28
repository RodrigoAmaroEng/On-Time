package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import dev.amaro.on_time.Samples
import dev.amaro.on_time.core.*
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.ui.TextResources
import kotlin.test.Test

class AppReducerTest {

    private val reducer = AppReducer()

    @Test
    fun `Select a task to work`() {
        val someTask = Samples.workingTask1
        val state = reducer.reduce(Actions.SetWorkingTask(someTask), AppState())
        assertThat(state.currentTask).isEqualTo(someTask)
    }

    @Test
    fun `Load results from query`() {
        val someTasks = listOf(Task("Task 1", "Description 1", TaskState.NOT_STARTED))
        val state = reducer.reduce(Actions.QueryResults(someTasks), AppState())
        assertThat(state.tasks).isEqualTo(someTasks)
    }

    @Test
    fun `Enable filter only my tasks`() {
        val state = reducer.reduce(Actions.FilterMine, AppState())
        assertThat(state.onlyMyTasks).isTrue()
    }

    @Test
    fun `Disable filter only my tasks`() {
        val state = reducer.reduce(Actions.FilterMine, AppState(onlyMyTasks = true))
        assertThat(state.onlyMyTasks).isFalse()
    }
    @Test
    fun `Change last result action`() {
        val state = reducer.reduce(Actions.UpdateLastResult(Results.NetworkError), AppState(onlyMyTasks = true))
        assertThat(state.lastResult).isEqualTo(Results.NetworkError)
    }

    @Test
    fun `Stop task action clears the current task`() {
        val state = reducer.reduce(Actions.StopTask, AppState(currentTask = Samples.workingTask1))
        assertThat(state.currentTask).isNull()
    }

    @Test
    fun `Navigate to configuration screen action`() {
        val state = reducer.reduce(Actions.Navigation.GoToSettings, AppState(onlyMyTasks = true))
        assertThat(state.screen).isEqualTo(Navigation.Configuration)
    }

    @Test
    fun `Navigate to main screen action`() {
        val state = reducer.reduce(Actions.Navigation.GoToMain, AppState(onlyMyTasks = true, screen = Navigation.Configuration))
        assertThat(state.screen).isEqualTo(Navigation.Main)
    }

    @Test
    fun `Save new configuration action`() {
        val configuration = Configuration()
        val state = reducer.reduce(Actions.SaveConfiguration(configuration), AppState(onlyMyTasks = true))
        assertThat(state.configuration).isEqualTo(configuration)
    }

    @Test
    fun `Incomplete configuration action`() {
        val feedback = Feedback(TextResources.Errors.NotAllSettingsWereInformed, FeedbackType.Error)
        val state = reducer.reduce(Actions.ProvideFeedback(feedback), AppState(onlyMyTasks = true))
        assertThat(state.feedback).isEqualTo(feedback)
    }
}