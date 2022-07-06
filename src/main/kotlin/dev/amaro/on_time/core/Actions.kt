package dev.amaro.on_time.core

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.sonic.IAction
import dev.amaro.sonic.ISideEffectAction

sealed class Actions : IAction {

    sealed class Navigation: Actions() {
        object GoToConfiguration: Navigation()
    }

    object NoAction : Actions()
    object Refresh : Actions()
    object StopTask: Actions()
    data class UpdateLastResult(val result: Results): Actions()
    object FilterMine : Actions(), ISideEffectAction {
        override val sideEffect: IAction = Refresh
    }
    data class QueryResults(val tasks: List<Task>) : Actions(), ISideEffectAction {
        override val sideEffect: IAction = UpdateLastResult(Results.Idle)
    }
    data class StartTask(val task: Task) : Actions()
    data class SetWorkingTask(val task: WorkingTask) : Actions()
    data class SetTaskState(val task: Task, val state: TaskState) : Actions(), ISideEffectAction {
        override val sideEffect: IAction = Refresh
    }
    data class AssignToMe(val task: Task) : Actions(), ISideEffectAction {
        override val sideEffect: IAction = Refresh
    }

}