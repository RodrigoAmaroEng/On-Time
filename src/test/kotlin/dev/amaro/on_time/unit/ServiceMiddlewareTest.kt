package dev.amaro.on_time.unit

import dev.amaro.on_time.Samples
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.Results
import dev.amaro.on_time.core.ServiceMiddleware
import dev.amaro.on_time.listActions
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.network.ConditionsBuilder
import dev.amaro.on_time.network.Connector
import dev.amaro.on_time.network.Jql
import dev.amaro.sonic.IProcessor
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import java.net.SocketException
import kotlin.test.Test


class ServiceMiddlewareTest {

    private val otherActions: List<Actions> = listActions(listOf(Actions.Refresh::class, Actions.SaveConfiguration::class))

    @Test
    fun `Fires when receives the refresh action and the configuration is valid`() {
        mockkObject(ConditionsBuilder)
        val condition = Jql.Builder().condition { assignee().isEmpty() }
        every { ConditionsBuilder.buildFrom(any()) } returns condition
        val connector: Connector = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.Refresh, AppState( configuration = Samples.configuration), processor)
        coVerify { connector.getTasks(eq(condition)) }
    }

    @Test
    fun `Do not fire when receives the refresh action but the configuration is invalid`() {
        mockkObject(ConditionsBuilder)
        val condition = Jql.Builder().condition { assignee().isEmpty() }
        every { ConditionsBuilder.buildFrom(any()) } returns condition
        val connector: Connector = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.Refresh, AppState(), processor)
        coVerify (exactly = 0) { connector.getTasks(eq(condition)) }
    }

    @Test
    fun `Do not fire when receives any other action`() {
        val connector: Connector = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        otherActions.forEach { middleware.process(it, AppState(), processor) }
        coVerify(exactly = 0) { connector.getTasks() }
    }

    @Test
    fun `Dispatch results to reducer`() {
        val connector: Connector = mockk(relaxed = true)
        val response = listOf(Task("1", "Task 1", TaskState.NOT_STARTED))
        every { connector.getTasks(any()) } returns response
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.Refresh, AppState(configuration = Samples.configuration), processor)
        coVerify {
            processor.reduce(Actions.UpdateLastResult(Results.Processing))
            processor.reduce(Actions.QueryResults(response))
        }
    }

    @Test
    fun `Dispatch error action when having a network error`() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(any()) } throws SocketException()
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.Refresh, AppState(configuration = Samples.configuration), processor)
        coVerify { processor.reduce(Actions.UpdateLastResult(Results.Errors.NetworkError)) }
    }
    @Test
    fun `Dispatch 'Processing' before making a network request`() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(any()) } throws SocketException()
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.Refresh, AppState(configuration = Samples.configuration), processor)
        coVerify { processor.reduce(Actions.UpdateLastResult(Results.Processing)) }
    }

    @Test
    fun `If receive UpdateConfiguration action updates the connector configuration`() {
        val connector: Connector = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.UpdateServiceConfiguration, AppState(configuration = Samples.configuration), processor)
        coVerify { connector.update(Samples.configuration) }
    }

    @Test
    fun `If receive UpdateConfiguration action but the configuration is empty, do nothing`() {
        val connector: Connector = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.UpdateServiceConfiguration, AppState(), processor)
        coVerify(exactly = 0) { connector.update(any()) }
    }
}



