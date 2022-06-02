package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


class JiraRequester(
    private val host: String,
    private val token: String
) {
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
    fun get(path: String): String? {
        val request: Request = Request.Builder()
            .url("https://$host$path")
            .header("Authorization", "Bearer $token")
            .get()
            .build()

        val response: Response = client.newCall(request).execute()
        return response.body?.string()
    }

    fun put(path: String, body: String): String? {

        val request: Request = Request.Builder()
            .url("https://$host$path")
            .header("Authorization", "Bearer $token")
            .put(body.toRequestBody("application/json".toMediaType()))
            .build()

        val response: Response = client.newCall(request).execute()
        return response.body?.string()
    }

    fun post(path: String, body: String): String? {

        val request: Request = Request.Builder()
            .url("https://$host$path")
            .header("Authorization", "Bearer $token")
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        val response: Response = client.newCall(request).execute()
        return response.body?.string()
    }
}

class JiraConnector(
    private val jira: JiraRequester
) : Connector {
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
        }.queryString(encode = true)

        val responseString = jira.get("/rest/agile/1.0/board/596/backlog?$body&fields=key,assignee,summary,status")

        return responseString?.let {
            json
                .decodeFromString<JiraResponse>(it)
                .issues
                .map { issue ->
                    Task(issue.key, issue.fields.summary, TaskState.fromJira(issue.fields.status.name))
                }
        } ?: emptyList()
    }

    override fun assign(task: Task, userName: String) {
        jira.put("/rest/api/2/issue/${task.id}/assignee", "{\"name\":\"$userName\"}")
    }
}

@kotlinx.serialization.Serializable
data class JiraStatus(val name: String)

@kotlinx.serialization.Serializable
data class JiraFields(val summary: String, val status: JiraStatus)

@kotlinx.serialization.Serializable
data class JiraTask(val key: String, val fields: JiraFields)


@kotlinx.serialization.Serializable
data class JiraResponse(val issues: List<JiraTask>, val total: Int, val maxResults: Int)