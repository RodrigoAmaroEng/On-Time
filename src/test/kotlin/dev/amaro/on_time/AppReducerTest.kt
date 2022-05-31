package dev.amaro.on_time

import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppReducer
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import kotlin.test.Test
import kotlin.test.assertEquals

class AppReducerTest {

    private val reducer = AppReducer()

    @Test
    fun `Select a task to work`() {
        val someTask = Task("Task 1", "Description 1", TaskState.UNASSIGNED)
        val state = reducer.reduce(Actions.StartTask(someTask), AppState())
        assertEquals(someTask , state.currentTask)
    }

    @Test
    fun `Load results from query`() {
        val someTasks = listOf(Task("Task 1", "Description 1", TaskState.UNASSIGNED))
        val state = reducer.reduce(Actions.QueryResults(someTasks), AppState())
        assertEquals(someTasks , state.tasks)
    }

}