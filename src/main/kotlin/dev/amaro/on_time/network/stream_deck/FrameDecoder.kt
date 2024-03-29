package dev.amaro.on_time.network.stream_deck

import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

typealias FrameAction = String

interface FrameDealer {
    fun parse(frame: Frame.Text): FrameAction
    fun encode(payload: Payload): Frame.Text
    suspend fun process(
        frame: Frame.Text,
        output: suspend (Payload) -> Unit,
        stateSelector: AppState,
        onAction: (Actions) -> Unit
    )
}

class JsonFrameDecoder : FrameDealer {
    private val json = Json {
        ignoreUnknownKeys = true
    }
    companion object {
        private val PACKAGE = Payload::class.java.packageName
    }

    override fun parse(frame: Frame.Text): FrameAction {
        val jsonObject: Map<String,String> = json.decodeFromString(frame.readText())
        return jsonObject["type"] ?: ""
    }

    override suspend fun process(
        frame: Frame.Text,
        output: suspend (Payload) -> Unit,
        stateSelector: AppState,
        onAction: (Actions) -> Unit
    ) {
        when (parse(frame)) {
            "LastTask" -> {
                println("Was a LastTask command")
                buildStateFrame(stateSelector)?.run { output(this) }
            }
            "Toggle" -> {
                println("Was a Toggle command")
                onAction(Actions.ToggleTask)
                buildStateFrame(stateSelector)?.run { output(this) }
            }
            else -> Unit
        }
    }

    override fun encode(payload: Payload): Frame.Text {
        val jsonString = json.encodeToString(payload)
            .replace("$PACKAGE.","")
        return Frame.Text(jsonString)
    }


    private fun buildStateFrame(state: AppState): Payload? {
        return if (state.currentTask != null) {
            LastTask(state.currentTask.task.id, state.currentTask.minutesWorked, true)
        } else if (state.lastTask != null) {
            LastTask(state.lastTask.id, state.lastTask.minutesWorked, false)
        } else {
            null
        }
    }

}