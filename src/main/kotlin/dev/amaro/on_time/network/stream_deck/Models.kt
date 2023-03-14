package dev.amaro.on_time.network.stream_deck

import kotlinx.serialization.Serializable

@Serializable
sealed interface Payload {
    val type: String
}

@Serializable
data class LastTask(
    val key: String,
    val minutes: Int,
    val active: Boolean

) : Payload {
    override val type: String get() = LastTask::class.java.simpleName
}