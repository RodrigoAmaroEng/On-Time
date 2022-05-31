package dev.amaro.on_time

import dev.amaro.on_time.log.*
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import io.mockk.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TaskLoggerTest {

    private val clock: Clock = spyk(Clock())
    private val storage: Storage = mockk(relaxed = true)
    private val logger = TaskLogger(storage, clock)

    @Test
    fun `Log task started`() {
        val currentDateTime = LocalDateTime.of(2022,5,20,13,52)
        every { clock.now() } returns currentDateTime
        logger.logStarted(Task("CST-123", "", TaskState.UNASSIGNED))
        verify { storage.include(LogEvent.TASK_START, "CST-123", currentDateTime) }
    }

    @Test
    fun `Log task started but was working on another one`() {
        val currentDateTime = LocalDateTime.of(2022,5,20,13,52)
        every { clock.now() } returns currentDateTime
        logger.logStarted(Task("CST-123", "", TaskState.UNASSIGNED), Task("CST-456", "", TaskState.UNASSIGNED))
        verify {
            storage.include(LogEvent.TASK_END, "CST-456", currentDateTime)
            storage.include(LogEvent.TASK_START, "CST-123", currentDateTime)
        }
    }

    @Test
    fun `Log task end`() {
        val currentDateTime = LocalDateTime.of(2022,5,20,13,52)
        every { clock.now() } returns currentDateTime
        logger.logEnd(Task("CST-123", "", TaskState.UNASSIGNED))
        verify { storage.include(LogEvent.TASK_END, "CST-123", currentDateTime) }
    }

    @Test
    fun checkConnection() {
        SQLiteStorage().apply {
            include(LogEvent.TASK_START, "CST-123", LocalDateTime.now())
        }
    }

}