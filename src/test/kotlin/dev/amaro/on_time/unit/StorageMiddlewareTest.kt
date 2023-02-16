package dev.amaro.on_time.unit

import dev.amaro.on_time.Samples.task1
import dev.amaro.on_time.Samples.task2
import dev.amaro.on_time.Samples.workingTask1
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.StorageMiddleware
import dev.amaro.on_time.listActions
import dev.amaro.on_time.log.Logger
import dev.amaro.on_time.log.TaskLogger
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.sonic.IProcessor
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test

class StorageMiddlewareTest {
    private val otherActions: List<Actions> = listActions(
        listOf(
            Actions.Refresh::class,
            Actions.StartTask::class,
            Actions.StopTask::class,
            Actions.StartPomodoro::class,
            Actions.StopPomodoro::class,
        )
    )

    @Test
    fun `Refresh action returns the current task`() {
        val logger: TaskLogger = mockk(relaxed = true)
        val workingTask: WorkingTask = mockk(relaxed = true)
        every { logger.getCurrentTask() } returns workingTask
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.Refresh, AppState(), processor)
        coVerify { processor.reduce(Actions.SetWorkingTask(workingTask)) }
    }


    @Test
    fun `Start task action register it on logger`() {
        val logger: TaskLogger = mockk(relaxed = true)
        val workingTask: WorkingTask = mockk(relaxed = true)
        every { logger.getCurrentTask() } returns workingTask
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StartTask(task1), AppState(), processor)
        coVerify { logger.logStarted(task1) }
    }

    @Test
    fun `Start task action register it on logger informing previous task`() {
        val logger: TaskLogger = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StartTask(task2), AppState(currentTask = workingTask1), processor)
        coVerify { logger.logStarted(task2, workingTask1) }
    }

    @Test
    fun `Do not fire when receives any other action`() {
        val logger: TaskLogger = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        otherActions.forEach { middleware.process(it, AppState(), processor) }
        coVerify(exactly = 0) {
            logger.logStarted(any(), any())
            logger.logStarted(any())
            logger.logEnd(any())
            logger.getCurrentTask()
        }
    }

    @Test
    fun `Stop task action register it on logger`() {
        val logger: TaskLogger = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StopTask, AppState(currentTask = workingTask1), processor)
        coVerify { logger.logEnd(workingTask1) }
    }

    @Test
    fun `If there's no current task should update the state`() {
        val logger: TaskLogger = mockk(relaxed = true)
        every{ logger.getCurrentTask() } returns null
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StopTask, AppState(currentTask = workingTask1), processor)
        coVerify { processor.reduce(Actions.StopTask) }
    }

    @Test
    fun `Start a pomodoro for the current task`() {
        val logger: Logger = mockk(relaxed = true)
        every { logger.getCurrentTask() } returns workingTask1
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StartPomodoro(task1), AppState(currentTask = workingTask1), processor)
        coVerify {
            logger.logStartedPomodoro(task1)
        }
    }

    @Test
    fun `Start a pomodoro for other task than the current one`() {
        val logger: Logger = mockk(relaxed = true)
        every { logger.getCurrentTask() } returns workingTask1
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StartPomodoro(task2), AppState(currentTask = workingTask1), processor)
        coVerify {
            logger.logStarted(task2, workingTask1)
            logger.logStartedPomodoro(task2)
        }
    }

    @Test
    fun `Start a pomodoro for a task with no current task`() {
        val logger: Logger = mockk(relaxed = true)
        every { logger.getCurrentTask() } returns workingTask1
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StartPomodoro(task2), AppState(), processor)
        coVerify {
            logger.logStarted(task2)
            logger.logStartedPomodoro(task2)
        }
    }

    @Test
    fun `Stop pomodoro`() {
        val logger: Logger = mockk(relaxed = true)
        every { logger.getCurrentTask() } returns workingTask1
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.StopPomodoro, AppState(currentTask = workingTask1), processor)
        coVerify {
            logger.logEndPomodoro(workingTask1.task)
        }
    }
}