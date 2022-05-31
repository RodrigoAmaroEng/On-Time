package dev.amaro.on_time.core

import dev.amaro.on_time.models.Task
import dev.amaro.sonic.IAction

sealed class Actions : IAction {
    object Refresh : Actions()
    data class QueryResults(val tasks: List<Task>) : Actions()
    data class StartTask(val task: Task) : Actions()
}