package dev.amaro.on_time.network


@kotlinx.serialization.Serializable
data class JiraStatus(val name: String)

@kotlinx.serialization.Serializable
data class JiraFields(val summary: String, val status: JiraStatus)

@kotlinx.serialization.Serializable
data class JiraTask(val key: String, val fields: JiraFields)

@kotlinx.serialization.Serializable
data class JiraResponse(val issues: List<JiraTask>, val total: Int, val maxResults: Int)