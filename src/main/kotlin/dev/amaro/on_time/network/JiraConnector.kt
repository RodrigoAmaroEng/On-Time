package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task


class JiraConnector(
    private val requester: JiraRequester,
    private val mapper: JiraMapper
) : Connector {

    override fun getTasks(conditions: Jql.Builder): List<Task> {

        val body = conditions.apply {
            and {
                project().eq("CST")
            }
            and {
                field("resolution").set("Unresolved")
            }
            and {
                field("Platform").set("Android")
            }
            orderBy("priority").asc()
            orderBy("updated").desc()
        }.build().queryString(encode = true)

        val path = "/rest/agile/1.0/board/596/backlog?$body&fields=key,assignee,summary,status"

        return requester
            .get<JiraResponse>(path)
            ?.issues
            ?.map { issue ->
                Task(
                    issue.key,
                    issue.fields.summary,
                    mapper.fromJiraState(issue.fields.status.name)
                )
            } ?: emptyList()
    }

    override fun assign(task: Task, userName: String) {
        requester.put(
            "/rest/api/2/issue/${task.id}/assignee",
            buildMap {
                put("name", userName)
            }
        )
    }

    override fun changeStatus(task: Task) {
        requester.post("/rest/api/2/issue/${task.id}/transitions", buildMap {
            put("transition", mapper.toJiraTransition(task.status).toString())
        })
    }
}
