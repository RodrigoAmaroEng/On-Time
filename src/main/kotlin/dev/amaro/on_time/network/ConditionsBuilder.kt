package dev.amaro.on_time.network

import dev.amaro.on_time.core.AppState

object ConditionsBuilder {
    fun buildFrom(state: AppState): Jql.Builder = Jql.Builder().condition {
        if (state.onlyMyTasks) {
            assignee().any(Value.USER)
        } else {
            assignee().any(Value.USER, Value.EMPTY)
        }
    }
}