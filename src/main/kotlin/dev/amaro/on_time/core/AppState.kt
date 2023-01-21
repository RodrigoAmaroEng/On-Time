package dev.amaro.on_time.core

import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.network.FilterDefinition
import dev.amaro.on_time.utilities.PreFilters

data class AppState(
    val lastResult: Results = Results.Idle,
    val currentTask: WorkingTask? = null,
    val tasks: List<Task> = emptyList(),
    val onlyMyTasks: Boolean = false,
    val configuration: Configuration? = null,
    val screen: Navigation = Navigation.Main,
    val feedback: Feedback? = null,
    val filterDefinition: FilterDefinition? = PreFilters.ANDROID_UNRESOLVED
)

data class Feedback(
    val body: String,
    val type: FeedbackType = FeedbackType.Info,
    val title: String? = null
)
enum class FeedbackType {
    Info,
    Error
}


sealed interface Results {
    object Idle : Results
    object NetworkError : Results
    object Processing : Results
}

sealed interface Navigation {
    object Main : Navigation
    object Configuration : Navigation
}