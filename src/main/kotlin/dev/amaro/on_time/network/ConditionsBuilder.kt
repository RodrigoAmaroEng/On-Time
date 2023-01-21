package dev.amaro.on_time.network

import dev.amaro.on_time.core.AppState

object ConditionsBuilder {
    fun buildFrom(state: AppState): Jql.Builder = Jql.Builder().apply {
        condition {
            if (state.onlyMyTasks) {
                assignee().any(Value.USER)
            } else {
                assignee().any(Value.USER, Value.EMPTY)
            }
        }
        if (state.configuration?.projects?.isNotBlank() == true) {
            val projects = state.configuration.projects.split(",").toTypedArray()
            and {
                project().any(*projects)
            }
        }
        state.filterDefinition?.conditions?.map {
            and {
                field(it.fieldName).from(it.operator, it.value)
            }
        }
        state.filterDefinition?.sortingRules?.map {
            orderBy(it.fieldName).from(it.direction)
        }
    }
}