package dev.amaro.on_time.utilities

import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.network.JiraStateDefinition
import java.time.LocalDateTime
import java.time.ZoneOffset


inline fun <reified T> Any.takeIfInstance(): T? =
    if (this is T) this else null

fun LocalDateTime.toDatabase(): Long =
    withSecond(0).toEpochSecond(ZoneOffset.UTC)
fun LocalDateTime.discardSecondsAndNanos(): LocalDateTime =
    withNano(0).withSecond(0)

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)

typealias JiraStateMap = Map<TaskState, JiraStateDefinition>
