package dev.amaro.on_time.core

import dev.amaro.on_time.log.TaskLogger
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class StorageMiddleware(private val logger: TaskLogger) : IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        when (action) {
            is Actions.StartTask -> {
                logger.logStarted(action.task, state.currentTask)
                refresh(processor)
            }
            is Actions.StopTask -> {
                state.currentTask?.run { logger.logEnd(this) }
                refresh(processor)
            }
            is Actions.Refresh -> {
                refresh(processor)
            }
        }
    }

    private fun refresh(processor: IProcessor<AppState>) {
        processor.reduce(
            logger.getCurrentTask()?.let { Actions.SetWorkingTask(it) } ?: Actions.StopTask
        )
    }
}