package dev.amaro.on_time.network

import dev.amaro.on_time.models.TaskState

class JiraMapper(
    private val stateMapping: Map<TaskState, JiraStateDefinition>
) {
    fun fromJiraState(name: String): TaskState =
        stateMapping.entries.firstOrNull { it.value.names.contains(name) }?.key ?: TaskState.UNDEFINED

    fun toJiraTransition(state: TaskState): Int = stateMapping[state]!!.transitionCode
}

data class JiraStateDefinition(
    val transitionCode: Int,
    val names: List<String>
) {
    constructor(transitionCode: Int, name: String): this(transitionCode, listOf(name))
}