package dev.amaro.on_time.log

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.WorkingTask
import java.time.temporal.ChronoUnit

class TaskLogger(private val storage: Storage, private val clock: Clock) {

    fun logStarted(task: Task, previousTask: WorkingTask? = null) {
        if (previousTask?.task?.id == task.id)  return
        val timestamp = clock.now()
        previousTask?.apply { logEnd(this) }
        storage.include(StoreItemTask(LogEvent.TASK_START, task.id, timestamp))
    }

    fun logEnd(task: WorkingTask) {
        val timestamp = clock.now()
        val minutes  = ChronoUnit.MINUTES.between(task.startedAt, timestamp)
        storage.include(StoreItemTask(LogEvent.TASK_END, task.task.id, timestamp, minutes))
    }

    fun getCurrentTask(): WorkingTask? {
        val lastEvent = storage.getOpen()
        return TODO()
//        return if (lastEvent?.type == LogEvent.TASK_START) {
//            storage.getOpen()
//        } else {
//            null
//        }
    }
}