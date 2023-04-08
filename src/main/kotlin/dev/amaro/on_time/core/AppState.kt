package dev.amaro.on_time.core

import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.network.FilterDefinition
import dev.amaro.on_time.ui.Icons
import dev.amaro.on_time.ui.Tags
import dev.amaro.on_time.utilities.PreFilters
import java.time.LocalDateTime

data class AppState(
    val lastResult: Results = Results.Idle,
    val currentTask: WorkingTask? = null,
    val tasks: List<Task> = emptyList(),
    val onlyMyTasks: Boolean = false,
    val configuration: Configuration? = null,
    val screen: Navigation = Navigation.Main,
    val feedback: Feedback? = null,
    val filterDefinition: FilterDefinition? = PreFilters.ANDROID_UNRESOLVED,
    val breakStartedAt: LocalDateTime? = null,
    val lastTask: Task? = null,
    val searchQuery: String? = null
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
    sealed interface Errors :Results {
        object NetworkError : Errors
        data class ServiceError(val message: String) : Errors
    }
    object Processing : Results
}

enum class Navigation(
    val title: String,
    val icon: String,
    val action: Actions.Navigation,
    val tag: String
) {
    Main("Tasks", Icons.TASKS, Actions.Navigation.GoToMain, Tags.MainTab),
    Configuration("Settings", Icons.SETTINGS, Actions.Navigation.GoToSettings, Tags.SettingsTab),
}