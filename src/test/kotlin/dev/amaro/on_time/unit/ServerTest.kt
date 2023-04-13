package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import dev.amaro.on_time.Samples
import dev.amaro.on_time.Samples.asWorkingTask
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.network.stream_deck.Server
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ServerTest {

    private val TOGGLE_MESSAGE = "{\"type\":\"Toggle\"}"

    private val client = HttpClient {
        install(WebSockets)
    }

    private val stateProvider: MutableStateFlow<AppState> = MutableStateFlow(AppState())

    private val onActionCallback: (Actions) -> Unit = mockk(relaxed = true)

    @BeforeTest
    fun setUp() {
        Server.main(stateProvider, onActionCallback)
    }

    @AfterTest
    fun tearDown() {
        client.close()
        Server.stop()
    }

    @Test
    fun `send the status when connect - Last Task`() = runBlocking {
        stateProvider.value = AppState(lastTask = Samples.task1)
        val message = connectAndPerform()
        assertThat(message).isEqualTo("{\"type\":\"LastTask\",\"key\":\"CST-123\",\"minutes\":0,\"active\":false}")
    }

    @Test
    fun `send the status when connect - Current Task`() = runBlocking {
        stateProvider.value = AppState(currentTask = asWorkingTask(Samples.task2, minutes = 5))
        val message = connectAndPerform()
        assertThat(message).isEqualTo("{\"type\":\"LastTask\",\"key\":\"CST-321\",\"minutes\":5,\"active\":true}")
    }

    @Test
    fun `Send toggle command makes it send the new status`() = runBlocking {
        stateProvider.value = AppState(lastTask = Samples.task1)
        val message = connectAndPerform(ClientAction.Send(TOGGLE_MESSAGE), ClientAction.Exit)
        verify { onActionCallback(Actions.ToggleTask) }
        assertThat(message).isEqualTo("{\"type\":\"LastTask\",\"key\":\"CST-123\",\"minutes\":0,\"active\":false}")
    }

    private suspend fun connectAndPerform(vararg actions: ClientAction = arrayOf(ClientAction.Exit)): String {
        val messageSlot = AtomicReference<String>()
        val nextActions = actions.toMutableList()
        var keepOn = true
        delay(500)
        println("Connecting")
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = Server.SERVER_PORT, path = "/ws") {
            while (keepOn) {
                val othersMessage = incoming.receive() as? Frame.Text ?: continue
                println("Client received ${nextActions[0]}" )
                messageSlot.set(othersMessage.readText())
                delay(100)
                when (val m = nextActions.removeAt(0)) {
                    ClientAction.Exit -> keepOn = false
                    is ClientAction.Send -> send(m.message)
                }
            }
        }
        return messageSlot.get()
    }
    sealed class ClientAction {
        object Exit : ClientAction()
        data class Send(val message: String) : ClientAction()
    }
}