package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.Samples.EMPTY
import dev.amaro.on_time.Samples.FALSE
import dev.amaro.on_time.Samples.KIND_END
import dev.amaro.on_time.Samples.KIND_START
import dev.amaro.on_time.Samples.STATUS
import dev.amaro.on_time.Samples.TASK_ID_1
import dev.amaro.on_time.Samples.TASK_ID_2
import dev.amaro.on_time.Samples.TRUE
import dev.amaro.on_time.Samples.asWorkingTask
import dev.amaro.on_time.Samples.task1
import dev.amaro.on_time.Samples.task2
import dev.amaro.on_time.Samples.workingTask1
import dev.amaro.on_time.log.*
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.utilities.discardNanos
import dev.amaro.ontime.log.Logs
import dev.amaro.ontime.log.Tasks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.BeforeTest

class TaskLoggerTest {

    private val clock: Clock = spyk(Clock())
    private val storage: Storage = mockk(relaxed = true)
    private val logger = TaskLogger(storage, clock)

    @BeforeTest
    fun setUp() {
        val fixedDateTime = LocalDateTime.now().discardNanos()
        every { clock.now() } returns fixedDateTime
    }

    @Test
    fun `Log task started`() {
        val currentDateTime = LocalDateTime.of(2022, 5, 20, 13, 52)
        every { clock.now() } returns currentDateTime
        logger.logStarted(task1)
        verify { storage.include(StoreItemTask(LogEvent.TASK_START, task1, currentDateTime)) }
    }

    @Test
    fun `Log task started but was working on another one`() {
        val currentDateTime = clock.now()
        val workingTask = asWorkingTask(task1, currentDateTime)
        every { clock.now() } returns currentDateTime.plusMinutes(1)
        logger.logStarted(task2, workingTask)
        verify {
            storage.include(StoreItemTask(LogEvent.TASK_END, task1, currentDateTime.plusMinutes(1), 1))
            storage.include(StoreItemTask(LogEvent.TASK_START, task2, currentDateTime.plusMinutes(1), 0))
        }
    }

    @Test
    fun `Log task end`() {
        val currentDateTime = clock.now()
        val workingTask = asWorkingTask(task1, currentDateTime)
        every { clock.now() } returns currentDateTime.plusMinutes(1)
        logger.logEnd(workingTask)
        verify { storage.include(StoreItemTask(LogEvent.TASK_END, task1, currentDateTime.plusMinutes(1), 1)) }
    }

    /** DATABASE **/

    @Test
    fun `Start to work on task`() {
        TestSQLiteStorage().run {
            TaskLogger(this, clock).logStarted(task1)
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()
            val timestamp = clock.now().toEpochSecond(ZoneOffset.UTC)
            val expectedLogs = listOf(Logs(TASK_ID_1, timestamp, KIND_START))
            val expectedTasks = listOf(Tasks(TASK_ID_1, EMPTY, STATUS, FALSE, 0, TRUE))
            assertThat(logs).isEqualTo(expectedLogs)
            assertThat(tasks).isEqualTo(expectedTasks)
        }
    }

    @Test
    fun `Start to work on task with a current one`() {
        TestSQLiteStorage().run {
            TaskLogger(this, clock).apply {
                logStarted(task1)
                logStarted(task2, asWorkingTask(task1, clock.now()))
            }
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()
            val timestamp = clock.now().toEpochSecond(ZoneOffset.UTC)
            val expectedLogs = listOf(
                Logs(TASK_ID_1, timestamp, KIND_START),
                Logs(TASK_ID_1, timestamp, KIND_END),
                Logs(TASK_ID_2, timestamp, KIND_START)
            )
            val expectedTasks = listOf(
                Tasks(TASK_ID_1, EMPTY, STATUS, FALSE, 0, FALSE),
                Tasks(TASK_ID_2, EMPTY, STATUS, FALSE, 0, TRUE)
            )
            assertThat(logs).isEqualTo(expectedLogs)
            assertThat(tasks).isEqualTo(expectedTasks)
        }
    }

    @Test
    fun `Start to work on task which is the current one`() {
        TestSQLiteStorage().run {
            TaskLogger(this, clock).apply {
                logStarted(task1)
                logStarted(task1, workingTask1)
            }
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()
            val timestamp = clock.now().toEpochSecond(ZoneOffset.UTC)
            val expectedLogs = listOf(
                Logs(TASK_ID_1, timestamp, KIND_START),
            )
            val expectedTasks = listOf(
                Tasks(TASK_ID_1, EMPTY, STATUS, FALSE, 0, TRUE)
            )
            assertThat(logs).isEqualTo(expectedLogs)
            assertThat(tasks).isEqualTo(expectedTasks)
        }
    }


    @Test
    fun `Finish working on a task`() {
        TestSQLiteStorage().run {
            val now = clock.now()
            val timestamp = now.toEpochSecond(ZoneOffset.UTC)
            TaskLogger(this, clock).apply {
                workOnTaskFor(task1, 25, this)
            }
            val endTimestamp = now.plusMinutes(25).toEpochSecond(ZoneOffset.UTC)
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()

            val expectedLogs = listOf(
                Logs(TASK_ID_1, timestamp, KIND_START),
                Logs(TASK_ID_1, endTimestamp, KIND_END)
            )
            val expectedTasks = listOf(
                Tasks(TASK_ID_1, EMPTY, STATUS, FALSE, 25, FALSE)
            )
            assertThat(logs).isEqualTo(expectedLogs)
            assertThat(tasks).isEqualTo(expectedTasks)
        }
    }

    @Test
    fun `Accumulate minutes worked`() {
        TestSQLiteStorage().run {
            val now = clock.now()
            val timestamp = now.toEpochSecond(ZoneOffset.UTC)
            TaskLogger(this, clock).apply {
                workOnTaskFor(task1, 25, this)
                workOnTaskFor(task1, 15, this)
            }
            val middleTimestamp = now.plusMinutes(25).toEpochSecond(ZoneOffset.UTC)
            val endTimestamp = now.plusMinutes(40).toEpochSecond(ZoneOffset.UTC)
            val logs = database.my_tasksQueries.showAllLogs().executeAsList()
            val tasks = database.my_tasksQueries.showAllTasks().executeAsList()

            val expectedLogs = listOf(
                Logs(TASK_ID_1, timestamp, KIND_START),
                Logs(TASK_ID_1, middleTimestamp, KIND_END),
                Logs(TASK_ID_1, middleTimestamp, KIND_START),
                Logs(TASK_ID_1, endTimestamp, KIND_END)
            )
            val expectedTasks = listOf(
                Tasks(TASK_ID_1, EMPTY, STATUS, FALSE, 40, FALSE)
            )
            assertThat(logs).isEqualTo(expectedLogs)
            assertThat(tasks).isEqualTo(expectedTasks)
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

    @Test
    fun `Get current working task`() {
        val time = clock.now()
        every { clock.now() } returns time
        TestSQLiteStorage().run {
            val currentTask = TaskLogger(this, clock)
                .apply { logStarted(task1) }
                .getCurrentTask()
            val expectedTask = workingTask1.copy(startedAt = time)
            assertThat(currentTask).isEqualTo(expectedTask)
        }
    }

    @Test
    fun `Log pomodoro start`() {
        val firstTime = clock.now().plusMinutes(1)
        val secondTime = clock.now().plusMinutes(2)
        TestSQLiteStorage().run {
            val currentTask = TaskLogger(this, clock)
                .apply {
                    every { clock.now() } returns firstTime
                    logStarted(task1)
                    every { clock.now() } returns secondTime
                    logStartedPomodoro(task1)
                }
                .getCurrentTask()
            val expectedTask = workingTask1.copy(startedAt = firstTime, pomodoroStartedAt = secondTime)
            assertThat(currentTask).isEqualTo(expectedTask)
        }
    }

    @Test
    fun `Log pomodoro end`() {
        val firstTime = clock.now().plusMinutes(1)
        val secondTime = clock.now().plusMinutes(2)
        val thirdTime = clock.now().plusMinutes(27)
        TestSQLiteStorage().run {
            val currentTask = TaskLogger(this, clock)
                .apply {
                    every { clock.now() } returns firstTime
                    logStarted(task1)
                    every { clock.now() } returns secondTime
                    logStartedPomodoro(task1)
                    every { clock.now() } returns thirdTime
                    logEndPomodoro(task1)
                }
                .getCurrentTask()
            val expectedTask = workingTask1.copy(startedAt = firstTime)
            assertThat(currentTask).isEqualTo(expectedTask)
        }
    }

    @Test
    fun `Log pomodoro start gets last pomodoro`() {
        val firstTime = clock.now().plusMinutes(1)
        val secondTime = clock.now().plusMinutes(2)
        val thirdTime = clock.now().plusMinutes(27)
        val fourthTime = clock.now().plusMinutes(32)
        TestSQLiteStorage().run {
            val currentTask = TaskLogger(this, clock)
                .apply {
                    every { clock.now() } returns firstTime
                    logStarted(task1)
                    every { clock.now() } returns secondTime
                    logStartedPomodoro(task1)
                    every { clock.now() } returns thirdTime
                    logEndPomodoro(task1)
                    every { clock.now() } returns fourthTime
                    logStartedPomodoro(task1)
                }
                .getCurrentTask()
            val expectedTask = workingTask1.copy(startedAt = firstTime, pomodoroStartedAt = fourthTime)
            assertThat(currentTask).isEqualTo(expectedTask)
        }
    }

}


