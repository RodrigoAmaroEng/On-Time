package dev.amaro.on_time.network

import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task


class JiraConnector(
    private val requester: JiraRequester,
    private val mapper: JiraMapper
) : Connector {

    override fun update(configuration: Configuration) {
        requester.update(configuration)
    }

    override fun getTasks(conditions: Jql.Builder): List<Task> {

        val body = conditions
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

}
