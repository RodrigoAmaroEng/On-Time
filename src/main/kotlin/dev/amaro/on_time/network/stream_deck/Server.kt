package dev.amaro.on_time.network.stream_deck

import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration

object Server {

    const val SERVER_PORT = 8489
    private var instance: ApplicationEngine? = null
    private val frameDealer: FrameDealer = JsonFrameDecoder()
    private const val fakeLastTaskCommand = "{\"type\":\"LastTask\"}"

    fun main(stateSelector: MutableStateFlow<AppState>, onAction: (Actions) -> Unit) {
        if (instance != null) return
        instance = embeddedServer(Netty, SERVER_PORT) {
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(15)
            }
            routing {
                webSocket("/ws") {
                    launch {
                        frameDealer.process(
                            Frame.Text(fakeLastTaskCommand),
                            { send(frameDealer.encode(it)) },
                            { stateSelector.value },
                            onAction
                        )
                        stateSelector.debounce(100L).collect     {
                            println(it)
                            frameDealer.process(
                                Frame.Text(fakeLastTaskCommand),
                                { send(frameDealer.encode(it)) },
                                { it },
                                onAction
                            )
                        }
                    }
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> frameDealer.process(
                                frame,
                                { send(frameDealer.encode(it)) },
                                { stateSelector.value },
                                onAction

                            )
                            else -> send(Frame.Text("Invalid frame"))
                        }
                    }
                }
            }
        }.apply { start(wait = false) }
    }

    fun stop() {
        instance?.stop(0, 0)
        instance = null
    }



}