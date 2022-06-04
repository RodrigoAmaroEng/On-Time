package dev.amaro.on_time.models

import dev.amaro.on_time.ui.Icons

enum class TaskState(val icon: String) {
    UNDEFINED(Icons.UNASSIGNED),
    NOT_STARTED(Icons.NOT_STARTED),
    WORKING(Icons.WORKING),
    ON_REVIEW(Icons.CODE_REVIEW),
    ON_QA(Icons.ON_QA),
    DONE(Icons.TASK_DONE);
}

data class Task(
    val id: String,
    val title: String,
    val status: TaskState,
    val isMine: Boolean = false
)






