package dev.amaro.on_time.core

import dev.amaro.sonic.IAction
import dev.amaro.sonic.IReducer

class AppReducer(private val debug: Boolean = false): IReducer<AppState> {
    override fun reduce(action: IAction, currentState: AppState): AppState {
        val nextState = when (action) {
            is Actions.FilterMine -> currentState.copy(onlyMyTasks = currentState.onlyMyTasks.not())
            is Actions.SetWorkingTask -> currentState.copy(currentTask = action.task)
            is Actions.QueryResults -> currentState.copy(tasks = action.tasks)
            is Actions.UpdateLastResult -> currentState.copy(lastResult = action.result)
            is Actions.StopTask -> currentState.copy(currentTask = null)
            is Actions.SaveConfiguration -> currentState.copy(
                configuration = action.configuration,
                screen = Navigation.Main
            )
            is Actions.Navigation.GoToSettings -> currentState.copy(screen = Navigation.Configuration)
            is Actions.Navigation.GoToMain -> currentState.copy(screen = Navigation.Main)
            is Actions.ProvideFeedback -> currentState.copy(feedback = action.feedback)
            is Actions.DismissFeedback -> currentState.copy(feedback = null)
            is Actions.StartBreak -> currentState.copy(breakStartedAt = action.at)
            is Actions.StopBreak -> currentState.copy(breakStartedAt = null)
            is Actions.SetLastTask -> currentState.copy(lastTask = action.task)
            is Actions.ClearLastTask -> currentState.copy(lastTask = null)
            else -> currentState
        }
        if (debug) {
            println(" # Action to Reduce: $action")
            println(" # Next state: $nextState")
        }
        return nextState
    }
}