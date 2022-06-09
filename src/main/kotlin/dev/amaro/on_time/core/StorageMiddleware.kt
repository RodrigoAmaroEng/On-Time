package dev.amaro.on_time.core

import dev.amaro.on_time.log.TaskLogger
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class StorageMiddleware(private val logger: TaskLogger): IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (action is Actions.StartTask) {
            logger.logStarted(action.task, state.currentTask)
            refresh(processor)
        } else if (action is Actions.Refresh) {
            refresh(processor)
        }
    }
    
    private fun refresh(processor: IProcessor<AppState>) {
        logger.getCurrentTask()?.let {
            processor.reduce(Actions.SetWorkingTask(it))
        }
    }
}