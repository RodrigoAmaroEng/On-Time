package dev.amaro.on_time.models

import dev.amaro.on_time.ui.Icons

enum class TaskState(val icon: String) {
    UNDEFINED(Icons.STATE_TODO),
    NOT_STARTED(Icons.STATE_TODO),
    WORKING(Icons.STATE_WORKING),
    ON_REVIEW(Icons.STATE_REVIEWING),
    ON_QA(Icons.STATE_QA);
}

data class Task(
    val id: String,
    val title: String,
    val status: TaskState,
    val isMine: Boolean = false,
    val minutesWorked : Int = 0,
)





