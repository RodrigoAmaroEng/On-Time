package dev.amaro.on_time.core

import dev.amaro.on_time.network.Connector
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class ServiceMiddleware(private val connector: Connector) : IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (action is Actions.Refresh) {
            val results = connector.getTasks()
            processor.reduce(Actions.QueryResults(results))
        }
    }
}