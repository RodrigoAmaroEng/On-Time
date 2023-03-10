package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.Samples
import dev.amaro.on_time.log.LogEvent
import dev.amaro.on_time.log.StoreItemTask
import dev.amaro.on_time.log.TestSQLiteStorage
import dev.amaro.on_time.utilities.discardSecondsAndNanos
import java.time.LocalDateTime
import kotlin.test.Test

class StorageTest {

    @Test
    fun `Start a task with pomodoro and retrieve the current task`() {
        val firstTime = LocalDateTime.now().plusMinutes(1).discardSecondsAndNanos()
        TestSQLiteStorage().run {
            include(StoreItemTask(LogEvent.TASK_START, Samples.task1, firstTime))
            include(StoreItemTask(LogEvent.POMODORO_START, Samples.task1, firstTime))
            val currentTask = getOpen()
            assertThat(currentTask?.task?.id).isEqualTo(Samples.TASK_ID_1)
            assertThat(currentTask?.startedAt).isEqualTo(firstTime)
            assertThat(currentTask?.pomodoroStartedAt).isEqualTo(firstTime)
        }
    }

    @Test
    fun `Get the last worked task`() {
        val firstTime = LocalDateTime.now().plusMinutes(1).discardSecondsAndNanos()
        TestSQLiteStorage().run {
            include(StoreItemTask(LogEvent.TASK_START, Samples.task1, firstTime))
            include(StoreItemTask(LogEvent.TASK_END, Samples.task1, firstTime.plusMinutes(1)))
            val lastTask = getLastTask()
            assertThat(lastTask?.id).isEqualTo(Samples.TASK_ID_1)
            assertThat(lastTask?.title).isEqualTo(Samples.task1.title)
        }
    }

    @Test
    fun `Get the last worked task is null because there an open task`() {
        val firstTime = LocalDateTime.now().plusMinutes(1).discardSecondsAndNanos()
        TestSQLiteStorage().run {
            include(StoreItemTask(LogEvent.TASK_START, Samples.task2, firstTime))
            include(StoreItemTask(LogEvent.TASK_END, Samples.task2, firstTime.plusMinutes(1)))
            include(StoreItemTask(LogEvent.TASK_START, Samples.task1, firstTime.plusMinutes(2)))
            val lastTask = getLastTask()
            assertThat(lastTask).isEqualTo(null)
        }
    }

    @Test
    fun `Get the last worked task is null because there an open task - same task context`() {
        val firstTime = LocalDateTime.now().plusMinutes(1).discardSecondsAndNanos()
        TestSQLiteStorage().run {
            include(StoreItemTask(LogEvent.TASK_START, Samples.task1, firstTime))
            include(StoreItemTask(LogEvent.TASK_END, Samples.task1, firstTime.plusMinutes(1)))
            include(StoreItemTask(LogEvent.TASK_START, Samples.task1, firstTime.plusMinutes(2)))
            val lastTask = getLastTask()
            assertThat(lastTask).isEqualTo(null)
        }
    }

}