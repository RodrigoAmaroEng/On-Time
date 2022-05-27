package dev.amaro.on_time.log

import java.time.LocalDateTime

data class Event(
    val kind: LogEvent,
    val timeStamp: LocalDateTime
)
