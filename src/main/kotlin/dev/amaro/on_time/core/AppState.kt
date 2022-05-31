package dev.amaro.on_time.core

import dev.amaro.on_time.models.Task

data class AppState(
    val currentTask: Task? = null,
    val tasks: List<Task> = emptyList()
)
