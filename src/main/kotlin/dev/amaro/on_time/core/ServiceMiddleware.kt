package dev.amaro.on_time.core

import dev.amaro.on_time.network.ConditionsBuilder
import dev.amaro.on_time.network.Connector
import dev.amaro.on_time.network.JiraException
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor
import java.net.SocketException

class ServiceMiddleware(private val connector: Connector) : IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (action is Actions.UpdateServiceConfiguration) {
            state.configuration?.run { connector.update(this) }
        } else if (action is Actions.Refresh && state.configuration?.isValid == true) {
            try {
                dispatchProcessing(processor)
                val results = connector.getTasks(ConditionsBuilder.buildFrom(state))
                processor.reduce(Actions.QueryResults(results))
            } catch (ex: JiraException) {
                dispatchError(processor, Results.Errors.ServiceError(ex.message!!))
            } catch (ex: SocketException) {
                dispatchError(processor, Results.Errors.NetworkError)
            }
        }
    }

    private fun dispatchError(processor: IProcessor<AppState>, error: Results.Errors) {
        processor.reduce(Actions.UpdateLastResult(error))
    }

    private fun dispatchProcessing(processor: IProcessor<AppState>) {
        processor.reduce(Actions.UpdateLastResult(Results.Processing))
    }
}