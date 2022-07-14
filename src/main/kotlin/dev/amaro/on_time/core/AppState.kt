package dev.amaro.on_time.core

import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.WorkingTask

data class AppState(
    val lastResult: Results = Results.Idle,
    val currentTask: WorkingTask? = null,
    val tasks: List<Task> = emptyList(),
    val onlyMyTasks: Boolean = false,
    val configuration: Configuration? = null,
    val screen: Navigation = Navigation.Main
)

sealed interface Results {
    object Idle : Results
    object NetworkError : Results
    object Processing : Results
}

sealed interface Navigation {
    object Main : Navigation
    object Configuration : Navigation
}