package dev.amaro.on_time.network.stream_deck

import kotlinx.serialization.Serializable

// TODO:
// Looks like the Payload is being used incorrectly.
// It's better to use a Map for both incoming and outgoing frames.
// Classes like LastTask act just like a helper to create the map
// What we really need is
// - a way to parse what command the server has received
// - a way to get the necessary data (according to the command)
// - a way to create the map to send back to the client.

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