package dev.amaro.on_time.network


@kotlinx.serialization.Serializable
data class JiraStatus(val name: String)

@kotlinx.serialization.Serializable
data class JiraAssignee(val name: String)

@kotlinx.serialization.Serializable
data class JiraFields(val summary: String, val status: JiraStatus, val assignee: JiraAssignee?)

@kotlinx.serialization.Serializable
data class JiraTask(val key: String, val fields: JiraFields)

@kotlinx.serialization.Serializable
data class JiraResponse(val issues: List<JiraTask>, val total: Int, val maxResults: Int)

@kotlinx.serialization.Serializable
data class JiraErrors(val errorMessages: List<String>, val errors: Map<String, String>)

class JiraException(errors: JiraErrors) : Exception(errors.errorMessages.first())