package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.network.JiraException
import dev.amaro.on_time.network.JiraRequester
import dev.amaro.on_time.network.JiraResponse
import io.mockk.every
import io.mockk.mockk
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import kotlin.test.BeforeTest
import kotlin.test.Test

class NetworkTest {
    private val configuration: Configuration = mockk(relaxed = true)
    private val client: OkHttpClient = mockk(relaxed = true)
    private val call : Call = mockk(relaxed = true)
    private val response: Response = mockk(relaxed = true)
    private val requester = JiraRequester(configuration, client)

    @BeforeTest
    fun setup() {
        every { configuration.host } returns "https://jira.cardlytics.com/rest/agile/1.0"
        every { client.newCall(any()) } returns call
        every { call.execute() } returns response
    }

    @Test
    fun `should return a list of tasks`() {
        val configuration: Configuration = mockk(relaxed = true)
        every { configuration.host } returns "https://jira.cardlytics.com/rest/agile/1.0"
        val client: OkHttpClient = mockk(relaxed = true)
        val call : Call = mockk(relaxed = true)
        val response: Response = mockk(relaxed = true)
        every { client.newCall(any()) } returns call
        every { call.execute() } returns response
        every { response.body?.string() } returns TASK_LIST_RESPONSE
        val requester = JiraRequester(configuration, client)
        val tasks = requester.get<JiraResponse>("")
        assertThat(tasks?.issues?.size).isEqualTo(2)
        assertThat(tasks?.issues?.first()?.key).isEqualTo("CST-370")
        assertThat(tasks?.issues?.first()?.fields?.summary).isEqualTo("As a developer I want to update the @Parcelize plugin imports")
        assertThat(tasks?.issues?.first()?.fields?.status?.name).isEqualTo("IN PROGRESS")
    }

    @Test
    fun `should return throw an exception`() {
        every { response.body?.string() } returns REQUEST_ERROR
        try {
            requester.get<JiraResponse>("")
        } catch (e: JiraException) {
            assertThat(e.message).isEqualTo("The issue key 'asd' for field 'key' is invalid.")
        }
    }
}

private val REQUEST_ERROR = "{\"errorMessages\":[\"The issue key 'asd' for field 'key' is invalid.\"],\"errors\":{}}"

private val TASK_LIST_RESPONSE = """
{
  "expand": "schema,names",
  "startAt": 0,
  "maxResults": 50,
  "total": 2,
  "issues": [
    {
      "expand": "operations,versionedRepresentations,editmeta,changelog,renderedFields",
      "id": "190828",
      "self": "https://jira.cardlytics.com/rest/agile/1.0/issue/190828",
      "key": "CST-370",
      "fields": {
        "summary": "As a developer I want to update the @Parcelize plugin imports",
        "assignee": null,
        "status": {
          "self": "https://jira.cardlytics.com/rest/api/2/status/3",
          "description": "This issue is being actively worked on at the moment by the assignee.",
          "iconUrl": "https://jira.cardlytics.com/images/icons/statuses/inprogress.png",
          "name": "IN PROGRESS",
          "id": "3",
          "statusCategory": {
            "self": "https://jira.cardlytics.com/rest/api/2/statuscategory/4",
            "id": 4,
            "key": "indeterminate",
            "colorName": "yellow",
            "name": "In Progress"
          }
        }
      }
    },
    {
      "expand": "operations,versionedRepresentations,editmeta,changelog,renderedFields",
      "id": "193360",
      "self": "https://jira.cardlytics.com/rest/agile/1.0/issue/193360",
      "key": "CST-417",
      "fields": {
        "summary": "As a developer I want to add a test to check the boost expiration time using a CountdownTimer",
        "assignee": null,
        "status": {
          "self": "https://jira.cardlytics.com/rest/api/2/status/1",
          "description": "The issue is open and ready for the assignee to start work on it.",
          "iconUrl": "https://jira.cardlytics.com/images/icons/statuses/open.png",
          "name": "ToDo",
          "id": "1",
          "statusCategory": {
            "self": "https://jira.cardlytics.com/rest/api/2/statuscategory/2",
            "id": 2,
            "key": "new",
            "colorName": "blue-gray",
            "name": "To Do"
          }
        }
      }
    }
  ]
}
""".trimIndent()