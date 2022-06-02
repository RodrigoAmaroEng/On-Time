package dev.amaro.on_time.models

enum class TaskState {
    UNDEFINED,
    NOT_STARTED,
    WORKING,
    ON_REVIEW,
    ON_QA,
    DONE;
}

data class Task(
    val id: String,
    val title: String,
    val status: TaskState
)






