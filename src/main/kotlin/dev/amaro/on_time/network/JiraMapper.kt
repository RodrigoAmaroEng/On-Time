package dev.amaro.on_time.network

import dev.amaro.on_time.models.TaskState

class JiraMapper(
    private val stateMapping: Map<TaskState, JiraStateDefinition>,
    private val currentUser: String
) {
    fun fromJiraState(name: String): TaskState =
        stateMapping.entries.firstOrNull { it.value.names.contains(name) }?.key ?: TaskState.UNDEFINED

    fun isFromCurrentUser(name: String?): Boolean = name == currentUser
}

data class JiraStateDefinition(
    val transitionCode: Int,
    val names: List<String>
) {
    constructor(transitionCode: Int, name: String): this(transitionCode, listOf(name))
}