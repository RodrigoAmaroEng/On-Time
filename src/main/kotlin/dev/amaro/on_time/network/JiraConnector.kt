package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
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

    override fun assignMe(task: Task) {
//        fetch("https://jira.cardlytics.com/secure/AssignIssue.jspa?atl_token=BOCK-EGM4-O733-9BCX_466bbd91591a6dddbe5181211ca699c48464b071_lin&id=202760&assignee=rodrigo.amaro&decorator=dialog&inline=true&_=1654042053255", {
//            "headers": {
//            "accept": "*/*",
//            "accept-language": "en-US,en;q=0.9",
//            "sec-ch-ua": "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"101\", \"Google Chrome\";v=\"101\"",
//            "sec-ch-ua-mobile": "?0",
//            "sec-ch-ua-platform": "\"macOS\"",
//            "sec-fetch-dest": "empty",
//            "sec-fetch-mode": "cors",
//            "sec-fetch-site": "same-origin",
//            "x-ausername": "rodrigo.amaro",
//            "x-requested-with": "XMLHttpRequest"
//        },
//            "referrer": "https://jira.cardlytics.com/projects/CST/issues/CST-558?filter=allopenissues&orderby=priority+ASC%2C+updated+DESC",
//            "referrerPolicy": "strict-origin-when-cross-origin",
//            "body": null,
//            "method": "GET",
//            "mode": "cors",
//            "credentials": "include"
//        });
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