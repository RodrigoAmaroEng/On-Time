package dev.amaro.on_time.log

import java.time.LocalDateTime

interface Storage {
    fun include(event: LogEvent, taskId: String, timeStamp: LocalDateTime)
}