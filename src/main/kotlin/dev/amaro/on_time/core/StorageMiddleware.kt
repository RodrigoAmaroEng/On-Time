package dev.amaro.on_time.core

import dev.amaro.on_time.log.Clock
import dev.amaro.on_time.log.Logger
import dev.amaro.on_time.utilities.Constants
import dev.amaro.sonic.IAction
import dev.amaro.sonic.IMiddleware
import dev.amaro.sonic.IProcessor

class StorageMiddleware(
    private val logger: Logger,
    private val clock: Clock
) : IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        val shouldRefresh = when (action) {
            is Actions.StartTask -> {
                startTask(action, state)
                true
            }

            is Actions.StartPomodoro -> {
                startPomodoro(state, action, processor)
                true
            }
            is Actions.StopPomodoro -> {
                stopPomodoro(state)
                true
            }

            is Actions.StopTask -> {
                stopTask(state)
                true
            }

            is Actions.Refresh -> true
            else -> false
        }
        if (shouldRefresh) {
            refresh(processor)
        }
    }

    private fun stopTask(state: AppState) {
        state.currentTask?.run { logger.logEnd(this) }
    }

    private fun stopPomodoro(state: AppState) {
        state.currentTask?.run { logger.logEndPomodoro(task) }
    }

    private fun startTask(action: Actions.StartTask, state: AppState) {
        logger.logStarted(action.task, state.currentTask)
    }

    private fun startPomodoro(state: AppState, action: Actions.StartPomodoro, processor: IProcessor<AppState>) {
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
        clock.startTimer(Constants.Pomodoro.FOCUS_TIME_IN_MINUTES) {
            // When the focus time is over, we need to stop the pomodoro and the task
            processor.perform(Actions.StopPomodoro)
            processor.perform(Actions.StopTask)
            // And start a break timer
            processor.reduce(Actions.StartBreak(clock.now()))
            clock.startTimer(Constants.Pomodoro.SHORT_BREAK_TIME_IN_MINUTES) {
                // When the break time is over, we signalize the end of the break
                processor.reduce(Actions.StopBreak)
            }
        }
    }

    private fun refresh(processor: IProcessor<AppState>) {
        processor.reduce(
            logger.getCurrentTask()?.let { Actions.SetWorkingTask(it) } ?: Actions.StopTask
        )
    }
}