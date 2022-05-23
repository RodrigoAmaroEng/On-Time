package dev.amaro.on_time.models

enum class TaskState {
    UNASSIGNED,
    WORKING,
    ON_REVIEW,
    ON_QA,
    DONE;

    companion object {
        fun fromJira(value: String): TaskState = when (value) {
            "ToDo" -> UNASSIGNED
            "IN PROGRESS" -> WORKING
            "IN CODE REVIEW" -> ON_REVIEW
            "In QA", "READY FOR QA" -> ON_QA
            else -> UNASSIGNED
        }
    }
}

data class Task(
    val id: String,
    val title: String,
    val status: TaskState
)






