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
import java.time.LocalDateTime
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
        val state = reducer.reduce(Actions.UpdateLastResult(Results.Errors.NetworkError), AppState(onlyMyTasks = true))
        assertThat(state.lastResult).isEqualTo(Results.Errors.NetworkError)
    }

    @Test
    fun `Search for text action`() {
        val state = reducer.reduce(Actions.Search("Something"), AppState(onlyMyTasks = true))
        assertThat(state.searchQuery).isEqualTo("Something")
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
        val state = reducer.reduce(Actions.SaveConfiguration(configuration), AppState(onlyMyTasks = true, screen = Navigation.Configuration))
        assertThat(state.configuration).isEqualTo(configuration)
        assertThat(state.screen).isEqualTo(Navigation.Main)
    }

    @Test
    fun `Incomplete configuration action`() {
        val feedback = Feedback(TextResources.Errors.NotAllSettingsWereInformed, FeedbackType.Error)
        val state = reducer.reduce(Actions.ProvideFeedback(feedback), AppState(onlyMyTasks = true))
        assertThat(state.feedback).isEqualTo(feedback)
    }

    @Test
    fun `Clear current feedback`() {
        val feedback = Feedback(TextResources.Errors.NotAllSettingsWereInformed, FeedbackType.Error)
        val state = reducer.reduce(Actions.DismissFeedback, AppState(onlyMyTasks = true, feedback = feedback))
        assertThat(state.feedback).isEqualTo(null)
    }

    @Test
    fun `Start the break timer`() {
        val now = LocalDateTime.now()
        val state = reducer.reduce(Actions.StartBreak(now), AppState())
        assertThat(state.breakStartedAt).isEqualTo(now)
    }

    @Test
    fun `Stop the break timer`() {
        val now = LocalDateTime.now()
        val state = reducer.reduce(Actions.StopBreak, AppState(breakStartedAt = now))
        assertThat(state.breakStartedAt).isEqualTo(null)
    }

    @Test
    fun `Set the last task`() {
        val state = reducer.reduce(Actions.SetLastTask(Samples.task1), AppState())
        assertThat(state.lastTask).isEqualTo(Samples.task1)
    }

    @Test
    fun `Clear last task`() {
        val state = reducer.reduce(Actions.ClearLastTask, AppState(lastTask = Samples.task1))
        assertThat(state.lastTask).isEqualTo(null)
    }
}