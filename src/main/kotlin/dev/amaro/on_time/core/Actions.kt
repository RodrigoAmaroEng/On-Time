package dev.amaro.on_time.core

import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.sonic.IAction
import dev.amaro.sonic.ISideEffectAction
import java.time.LocalDateTime

sealed interface Actions : IAction {

    object NoAction : Actions
    object Refresh : Actions
    object StopTask: Actions
    data class SaveConfiguration(val configuration: Configuration): Actions, ISideEffectAction {
        override val sideEffect: IAction = UpdateServiceConfiguration
    }
    object UpdateServiceConfiguration: Actions, ISideEffectAction {
        override val sideEffect: IAction = Refresh
    }
    data class ProvideFeedback(val feedback: Feedback): Actions
    object DismissFeedback: Actions

    data class UpdateLastResult(val result: Results) : Actions
    object FilterMine : Actions, ISideEffectAction {
        override val sideEffect: IAction = Refresh
    }

    data class QueryResults(val tasks: List<Task>) : Actions, ISideEffectAction {
        override val sideEffect: IAction = UpdateLastResult(Results.Idle)
    }

    data class StartTask(val task: Task) : Actions
    object StopPomodoro : Actions
    data class StartBreak(val at: LocalDateTime) : Actions
    object StopBreak : Actions
    data class StartPomodoro(val task: Task) : Actions
    data class SetWorkingTask(val task: WorkingTask) : Actions
    data class SetLastTask(val task: Task) : Actions
    object ClearLastTask : Actions
    data class SetTaskState(val task: Task, val state: TaskState) : Actions, ISideEffectAction {
        override val sideEffect: IAction = Refresh
    }

    data class AssignToMe(val task: Task) : Actions, ISideEffectAction {
        override val sideEffect: IAction = Refresh
    }

    sealed interface Navigation : Actions {
        object GoToSettings : Navigation
        object GoToMain: Navigation
    }

}