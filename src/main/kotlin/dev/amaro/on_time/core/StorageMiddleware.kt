package dev.amaro.on_time.core

import dev.amaro.on_time.log.Logger
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class StorageMiddleware(private val logger: Logger) : IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        val shouldRefresh = when (action) {
            is Actions.StartTask -> {
                logger.logStarted(action.task, state.currentTask)
                true
            }

            is Actions.StartPomodoro -> {
                // If there's a task being tracked
                state.currentTask?.run {
                    // And it's not the same as the one being started
                    if (action.task != task) {
                        // Then we need to log the end of the previous task along with the new one
                        logger.logStarted(action.task, this)
                    }

                }
                // If there's no task being tracked, just log the new one
                    ?: logger.logStarted(action.task)
                // And then log the start of the pomodoro
                logger.logStartedPomodoro(action.task)
                true
            }
            is Actions.StopPomodoro -> {
                state.currentTask?.run { logger.logEndPomodoro(task) }
                true
            }

            is Actions.StopTask -> {
                state.currentTask?.run { logger.logEnd(this) }
                true
            }

            is Actions.Refresh -> true
            else -> false
        }
        if (shouldRefresh) {
            refresh(processor)
        }
    }

    private fun refresh(processor: IProcessor<AppState>) {
        processor.reduce(
            logger.getCurrentTask()?.let { Actions.SetWorkingTask(it) } ?: Actions.StopTask
        )
    }
}