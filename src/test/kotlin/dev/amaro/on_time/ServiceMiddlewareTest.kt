package dev.amaro.on_time

import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.ServiceMiddleware
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.network.Connector
import dev.amaro.sonic.IProcessor
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test


class ServiceMiddlewareTest {

    private val otherActions: List<Actions> = listActions(listOf(Actions.Refresh::class))

    @Test
    fun `Fires when receives the refresh action`() {
        val connector: Connector = mockk(relaxed = true)
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.Refresh, AppState(), processor)
        coVerify { connector.getTasks() }
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
        val response = listOf(Task("1", "Task 1", TaskState.UNASSIGNED))
        every { connector.getTasks() } returns response
        val processor: IProcessor<AppState> = mockk(relaxed = true)
        val middleware = ServiceMiddleware(connector)
        middleware.process(Actions.Refresh, AppState(), processor)
        coVerify { processor.reduce(Actions.QueryResults(response)) }
    }

}



