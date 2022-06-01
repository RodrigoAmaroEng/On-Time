package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState


class JiraConnector(
    private val jira: JiraRequester
) : Connector {

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

        val path = "/rest/agile/1.0/board/596/backlog?$body&fields=key,assignee,summary,status"

        return jira
            .get<JiraResponse>(path)
            ?.issues
            ?.map { issue ->
                Task(
                    issue.key,
                    issue.fields.summary,
                    TaskState.fromJira(issue.fields.status.name)
                )
            } ?: emptyList()
    }

    override fun assign(task: Task, userName: String) {
        jira.put(
            "/rest/api/2/issue/${task.id}/assignee",
            buildMap {
                put("name", userName)
            }
        )
    }
}
