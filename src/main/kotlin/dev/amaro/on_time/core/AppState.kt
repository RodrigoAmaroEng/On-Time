package dev.amaro.on_time.core

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.WorkingTask

data class AppState(
    val currentTask: WorkingTask? = null,
    val tasks: List<Task> = emptyList(),
    val onlyMyTasks: Boolean = false,
)
