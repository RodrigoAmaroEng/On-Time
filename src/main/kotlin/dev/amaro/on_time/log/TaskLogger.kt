package dev.amaro.on_time.log

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.WorkingTask
import java.time.temporal.ChronoUnit

class TaskLogger(private val storage: Storage, private val clock: Clock) : Logger {

    override fun logStarted(task: Task, previousTask: WorkingTask?) {
        if (previousTask?.task?.id == task.id)  return
        val timestamp = clock.now()
        previousTask?.apply { logEnd(this) }
        storage.include(StoreItemTask(LogEvent.TASK_START, task, timestamp))
    }

    override fun logEnd(task: WorkingTask) {
        val timestamp = clock.now()
        val minutes  = ChronoUnit.MINUTES.between(task.startedAt, timestamp)
        storage.include(StoreItemTask(LogEvent.TASK_END, task.task, timestamp, minutes))
    }

    override fun getCurrentTask(): WorkingTask? {
        return storage.getOpen()
    }

    override fun logStartedPomodoro(task: Task) {
        val timestamp =  clock.now()
        storage.include(StoreItemTask(LogEvent.POMODORO_START, task, timestamp))
    }

    override fun logEndPomodoro(task: Task) {
        val timestamp =  clock.now()
        storage.include(StoreItemTask(LogEvent.POMODORO_END, task, timestamp))
    }
}
interface Logger {
    fun logStarted(task: Task, previousTask: WorkingTask? = null)
    fun logEnd(task: WorkingTask)
    fun getCurrentTask(): WorkingTask?
    fun logStartedPomodoro(task: Task)
    fun logEndPomodoro(task: Task)
}