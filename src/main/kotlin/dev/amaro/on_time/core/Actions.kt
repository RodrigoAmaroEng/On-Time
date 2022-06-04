package dev.amaro.on_time.core

import dev.amaro.on_time.models.Task
import dev.amaro.sonic.IAction

sealed class Actions(val sideEffect: Actions? = null) : IAction {
    object Refresh : Actions()
    object FilterMine: Actions(Refresh)
    data class QueryResults(val tasks: List<Task>) : Actions()
    data class StartTask(val task: Task) : Actions()
    data class SetTaskState(val task: Task) : Actions(Refresh)
    data class AssignToMe(val task: Task) : Actions(Refresh)

}