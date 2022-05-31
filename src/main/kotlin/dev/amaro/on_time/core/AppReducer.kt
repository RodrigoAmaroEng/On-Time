package dev.amaro.on_time.core

import dev.amaro.sonic.IAction
import dev.amaro.sonic.IReducer

class AppReducer: IReducer<AppState> {
    override fun reduce(action: IAction, currentState: AppState): AppState {
        return when (action) {
            is Actions.StartTask -> currentState.copy(currentTask = action.task)
            is Actions.QueryResults -> currentState.copy(tasks = action.tasks)
            else -> currentState
        }
    }
}