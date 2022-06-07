package dev.amaro.on_time.log

import dev.amaro.on_time.models.Task
import java.time.LocalDateTime

interface Storage {
    fun include(storeItem : StoreItemTask)

    fun getOpen(): Task?
}

data class StoreItemTask(
    val event: LogEvent,
    val taskId: String,
    val timeStamp: LocalDateTime,
    val minutes: Long = 0
)