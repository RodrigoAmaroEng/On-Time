package dev.amaro.on_time.unit

import dev.amaro.on_time.Samples.task1
import dev.amaro.on_time.Samples.task2
import dev.amaro.on_time.Samples.workingTask1
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.StorageMiddleware
import dev.amaro.on_time.listActions
import dev.amaro.on_time.log.TaskLogger
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.sonic.IProcessor
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class StorageMiddlewareTest {
    private val otherActions: List<Actions> = listActions(
        listOf(
            Actions.Refresh::class,
            Actions.StartTask::class
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
    fun `Refresh action does not fire if there is no current task`() {
        val logger: TaskLogger = mockk(relaxed = true)
        every { logger.getCurrentTask() } returns null
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = StorageMiddleware(logger)
        middleware.process(Actions.Refresh, AppState(), processor)
        coVerify (exactly = 0) { processor.reduce(any()) }
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
}