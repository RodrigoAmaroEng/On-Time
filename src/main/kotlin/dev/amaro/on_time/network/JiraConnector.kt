package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType


class JiraRequester(
    private val host: String,
    private val cookie: String
) {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    private val mediaType: MediaType = "application/x-www-form-urlencoded; charset=UTF-8".toMediaType()
    fun send(path: String, bodyString: String): String? {
        val body: RequestBody = RequestBody.create(
            mediaType,
            bodyString
        )
        val request: Request = Request.Builder()
            .url("https://$host$path")
            .method("POST", body)
            .addHeader("authority", host)
            .addHeader("accept", "application/json, text/javascript, */*; q=0.01")
            .addHeader("accept-language", "en-US,en;q=0.9")
            .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("cookie", cookie)
            .addHeader("origin", "https://$host")
            .addHeader("sec-fetch-mode", "cors")
            .addHeader("sec-fetch-site", "same-origin")
            .addHeader("x-atlassian-token", "no-check")
            .addHeader("x-requested-with", "XMLHttpRequest")
            .build()

        val response: Response = client.newCall(request).execute()

        return response.body?.string()
    }
}

class JiraConnector(
    private val jira: JiraRequester
) : Connector {
    companion object {
        const val path: String = "/rest/issueNav/1/issueTable"
    }

    private val json = Json { ignoreUnknownKeys = true }

    override fun getTasks(): List<Task> {
        val body = withJql {
            condition {
                project().eq("CST")
            }
            and {
                field("resolution").set("Unresolved")
            }
            and {
                field("Platform").set("Android")
            }
            and {
                assignee().any(Value.EMPTY, Value.USER)
            }
            orderBy("priority").asc()
            orderBy("updated").desc()
        }.queryString('+', true)

        return jira.send(path, body)?.let {
            json
                .decodeFromString<JiraResponse>(it)
                .issueTable
                .table
                .map { issue ->
                    Task(issue.key, issue.summary, TaskState.fromJira(issue.status))
                }
        } ?: emptyList()
    }
}
@kotlinx.serialization.Serializable
data class JiraTask(val key: String, val summary: String, val status: String)

@kotlinx.serialization.Serializable
data class JiraBody(val table: List<JiraTask>)

@kotlinx.serialization.Serializable
data class JiraResponse(val issueTable: JiraBody)