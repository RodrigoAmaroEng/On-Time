package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import dev.amaro.on_time.Samples
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppReducer
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Results
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
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
}