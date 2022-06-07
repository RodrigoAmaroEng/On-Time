package dev.amaro.on_time

import dev.amaro.on_time.Samples.FALSE
import dev.amaro.on_time.Samples.KIND_END
import dev.amaro.on_time.Samples.KIND_START
import dev.amaro.on_time.Samples.TASK_ID_1
import dev.amaro.on_time.Samples.TASK_ID_2
import dev.amaro.on_time.Samples.TRUE
import dev.amaro.on_time.Samples.asWorkingTask
import dev.amaro.on_time.log.*
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.ontime.log.Logs
import dev.amaro.ontime.log.Tasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals

class TaskLoggerTest {

    private val clock: Clock = spyk(Clock())
    private val storage: Storage = mockk(relaxed = true)
    private val logger = TaskLogger(storage, clock)

    @Test
    fun `Log task started`() {
        val currentDateTime = LocalDateTime.of(2022, 5, 20, 13, 52)
        every { clock.now() } returns currentDateTime
        logger.logStarted(Samples.task1)
        verify { storage.include(StoreItemTask(LogEvent.TASK_START, TASK_ID_1, currentDateTime)) }
    }

    @Test
    fun `Log task started but was working on another one`() {
        val currentDateTime = LocalDateTime.now()
        every { clock.now() } returns currentDateTime.plusMinutes(1)

        logger.logStarted(Samples.task2, Samples.workingTask1)
        verify {
            storage.include(StoreItemTask(LogEvent.TASK_END, TASK_ID_1, currentDateTime.plusMinutes(1), 1))
            storage.include(StoreItemTask(LogEvent.TASK_START, TASK_ID_2, currentDateTime.plusMinutes(1), 0))
        }
    }

    @Test
    fun `Log task end`() {
        val currentDateTime = LocalDateTime.now()
        every { clock.now() } returns currentDateTime.plusMinutes(1)
        logger.logEnd(Samples.workingTask1)
        verify { storage.include(StoreItemTask(LogEvent.TASK_END, TASK_ID_1, currentDateTime.plusMinutes(1), 0)) }
    }

    /** DATABASE **/

    @Test
    fun `Start to work on task`() {
        TestSQLiteStorage().run {
            TaskLogger(this, clock).logStarted(Samples.task1)
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()
            val timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            assertEquals(listOf(Logs(TASK_ID_1, timestamp, KIND_START)), logs)
            assertEquals(listOf(Tasks(TASK_ID_1, 0, TRUE)), tasks)
        }
    }

    @Test
    fun `Start to work on task with a current one`() {
        TestSQLiteStorage().run {
            TaskLogger(this, clock).apply {
                logStarted(Samples.task1)
                logStarted(Samples.task2, Samples.workingTask1)
            }
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()
            val timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            assertEquals(
                listOf(
                    Logs(TASK_ID_1, timestamp, KIND_START),
                    Logs(TASK_ID_1, timestamp, KIND_END),
                    Logs(TASK_ID_2, timestamp, KIND_START)
                ),
                logs
            )
            assertEquals(
                listOf(
                    Tasks(TASK_ID_1, 0, FALSE),
                    Tasks(TASK_ID_2, 0, TRUE)
                ),
                tasks
            )
        }
    }

    @Test
    fun `Start to work on task which is the current one`() {
        TestSQLiteStorage().run {
            TaskLogger(this, clock).apply {
                logStarted(Samples.task1)
                logStarted(Samples.task1, Samples.workingTask1)
            }
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()
            val timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            assertEquals(
                listOf(
                    Logs(TASK_ID_1, timestamp, KIND_START),
                ),
                logs
            )
            assertEquals(
                listOf(
                    Tasks(TASK_ID_1, 0, TRUE)
                ),
                tasks
            )
        }
    }


    @Test
    fun `Finish working on a task`() {
        TestSQLiteStorage().run {
            val now = LocalDateTime.now()
            val timestamp = now.toEpochSecond(ZoneOffset.UTC)
            TaskLogger(this, clock).apply {
                workOnTaskFor(Samples.task1, 25, this)
            }
            val endTimestamp = now.plusMinutes(25).toEpochSecond(ZoneOffset.UTC)
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()

            assertEquals(
                listOf(
                    Logs(TASK_ID_1, timestamp, KIND_START),
                    Logs(TASK_ID_1, endTimestamp, KIND_END)
                ),
                logs
            )
            assertEquals(
                listOf(
                    Tasks(TASK_ID_1, 25, FALSE)
                ),
                tasks
            )
        }
    }

    @Test
    fun `Accumulate minutes worked`() {
        TestSQLiteStorage().run {
            val now = LocalDateTime.now()
            val timestamp = now.toEpochSecond(ZoneOffset.UTC)
            TaskLogger(this, clock).apply {
                workOnTaskFor(Samples.task1, 25, this)
                workOnTaskFor(Samples.task1, 15, this)
            }
            val middleTimestamp = now.plusMinutes(25).toEpochSecond(ZoneOffset.UTC)
            val endTimestamp = now.plusMinutes(40).toEpochSecond(ZoneOffset.UTC)
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()

            assertEquals(
                listOf(
                    Logs(TASK_ID_1, timestamp, KIND_START),
                    Logs(TASK_ID_1, middleTimestamp, KIND_END),
                    Logs(TASK_ID_1, middleTimestamp, KIND_START),
                    Logs(TASK_ID_1, endTimestamp, KIND_END)
                    ),
                logs
            )
            assertEquals(
                listOf(
                    Tasks(TASK_ID_1, 40, FALSE)
                ),
                tasks
            )
        }
    }

    private fun workOnTaskFor(task: Task, duration: Long, logger: TaskLogger) {
        val now = clock.now()
        logger.apply {
            logStarted(task)
            every { clock.now() } returns now.plusMinutes(duration)
            logEnd(asWorkingTask(task, now))
        }
    }
}


object Samples {
    const val TASK_ID_1 = "CST-123"
    const val TASK_ID_2 = "CST-321"
    const val TASK_ID_3 = "CST-456"
    const val KIND_START = "S"
    const val KIND_END = "F"
    const val EMPTY = ""
    const val TRUE = 1L
    const val FALSE = 0L
    val task1 = Task(TASK_ID_1, EMPTY, TaskState.NOT_STARTED)
    val task2 = Task(TASK_ID_2, EMPTY, TaskState.NOT_STARTED)
    val task3 = Task(TASK_ID_3, EMPTY, TaskState.NOT_STARTED)

    val workingTask1 = asWorkingTask(task1)
    fun asWorkingTask(task: Task, startedAt: LocalDateTime = LocalDateTime.now(), minutes: Int = 0) =
        WorkingTask(task, startedAt, minutes)
}