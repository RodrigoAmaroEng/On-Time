package dev.amaro.on_time.log

import dev.amaro.on_time.models.Task

class TaskLogger(private val storage: Storage, private val clock: Clock) {

    fun logStarted(task: Task, previousTask: Task? = null) {
        val timestamp = clock.now()
        previousTask?.apply {
            logEnd(this)
        }
        storage.include(LogEvent.TASK_START, task.id, timestamp)
    }

    fun logEnd(task: Task) {
        val timestamp = clock.now()
        storage.include(LogEvent.TASK_END, task.id, timestamp)
    }

}