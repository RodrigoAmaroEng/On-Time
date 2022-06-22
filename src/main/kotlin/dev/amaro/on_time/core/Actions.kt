package dev.amaro.on_time.core

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.sonic.IAction

sealed class Actions(val sideEffect: Actions? = null) : IAction {

    object NoAction : Actions()
    object Refresh : Actions()
    object FilterMine: Actions(Refresh)
    data class QueryResults(val tasks: List<Task>) : Actions()
    data class StartTask(val task: Task) : Actions()
    data class SetWorkingTask(val task: WorkingTask) : Actions()
    data class SetTaskState(val task: Task, val state: TaskState) : Actions(Refresh)
    data class AssignToMe(val task: Task) : Actions(Refresh)

}