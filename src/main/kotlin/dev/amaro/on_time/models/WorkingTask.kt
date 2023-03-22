package dev.amaro.on_time.models

import java.time.LocalDateTime

data class WorkingTask(
    val task: Task,
    val startedAt: LocalDateTime,
    val minutesWorked: Int = 0,
    val pomodoroStartedAt: LocalDateTime? = null,
)
