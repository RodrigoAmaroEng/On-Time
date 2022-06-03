package dev.amaro.on_time.core

import dev.amaro.on_time.network.Connector
import dev.amaro.on_time.network.Jql
import dev.amaro.on_time.network.Value
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class ServiceMiddleware(private val connector: Connector) : IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (action is Actions.Refresh) {
            val conditions: Jql.Builder = Jql.Builder().condition {
                if (state.onlyMyTasks) {
                    assignee().any(Value.USER)
                } else {
                    assignee().any(Value.USER, Value.EMPTY)
                }
            }
            val results = connector.getTasks(conditions)
            processor.reduce(Actions.QueryResults(results))
        }
    }
}