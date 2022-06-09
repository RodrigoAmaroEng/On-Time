package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task


class JiraConnector(
    private val requester: JiraRequester,
    private val mapper: JiraMapper
) : Connector {

    override fun getTasks(conditions: Jql.Builder): List<Task> {

        val body = conditions
            .apply {
                and { project().eq("CST") }
                and { field("resolution").set("Unresolved") }
                and { field("Platform").set("Android") }
                orderBy("priority").desc()
                orderBy("updated").desc()
            }
            .build()
            .queryString(encode = true)

        val path = "/rest/api/2/search?$body&fields=key,assignee,summary,status,epic"

        return requester
            .get<JiraResponse>(path)
            ?.issues
            ?.map { issue ->
                Task(
                    issue.key,
                    issue.fields.summary,
                    mapper.fromJiraState(issue.fields.status.name),
                    mapper.isFromCurrentUser(issue.fields.assignee?.name)
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
