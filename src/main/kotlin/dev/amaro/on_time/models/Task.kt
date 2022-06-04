package dev.amaro.on_time.models

import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.ui.ActionDef
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
) {
    val actionsAvailable: List<ActionDef> = mutableListOf<ActionDef>()
        .apply {
            if (!isMine) {
                add(ActionDef(Icons.USER_ASSIGN, Actions.AssignToMe(this@Task)))
            }
            if (status !in arrayOf(TaskState.UNDEFINED, TaskState.ON_QA) ) {
                add(ActionDef(Icons.NOT_STARTED, Actions.SetTaskState(this@Task)))
            }
        }
}






