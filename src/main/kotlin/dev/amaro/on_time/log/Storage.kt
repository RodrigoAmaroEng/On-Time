package dev.amaro.on_time.log

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.WorkingTask
import java.time.LocalDateTime

interface Storage {
    fun include(storeItem : StoreItemTask)

    fun getOpen(): WorkingTask?
}

data class StoreItemTask(
    val event: LogEvent,
    val task: Task,
    val timeStamp: LocalDateTime,
    val minutes: Long = 0
)