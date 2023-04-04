package dev.amaro.on_time.network.stream_deck

import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.time.Duration

object Server {

    const val SERVER_PORT = 8489
    private var instance: ApplicationEngine? = null
    private val frameDealer: FrameDealer = JsonFrameDecoder()
    private val fakeLastTaskCommand = Frame.Text("{\"type\":\"LastTask\"}")

    fun main(stateSelector: MutableStateFlow<AppState>, onAction: (Actions) -> Unit) {
        if (instance != null) return
        instance = embeddedServer(Netty, SERVER_PORT) {
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(15)
            }
            routing {
                webSocket("/ws") {
                    process(stateSelector.value, onAction, fakeLastTaskCommand)
                    launch { stateSelector.debounce(100L).collect { process(it, onAction, fakeLastTaskCommand) } }
                    for (frame in incoming) {
                        println("Server received: ${frameDealer.parse(frame as Frame.Text)}")
                        when (frame) {
                            is Frame.Text -> process(stateSelector.value, onAction, frame)
                            else -> send(Frame.Text("Invalid frame"))
                        }
                    }
                    println("Disconnected")
                }
            }
        }.apply { start(wait = false) }
    }

    private suspend fun DefaultWebSocketServerSession.process(
        appState: AppState,
        onAction: (Actions) -> Unit,
        receivedFrame: Frame.Text
    ) {
        frameDealer.process(
            receivedFrame,
            {
                println("Server sending: $it")
                send(frameDealer.encode(it))
            },
            appState,
            onAction
        )
    }

    fun stop() {
        instance?.stop(0, 0)
        instance = null
    }


}