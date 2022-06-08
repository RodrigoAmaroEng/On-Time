package dev.amaro.on_time.utilities

import java.time.LocalDateTime
import java.time.ZoneOffset


inline fun <reified T> Any.takeIfInstance(): T? =
    if (this is T) this else null

fun LocalDateTime.toDatabase(): Long =
    withSecond(0).toEpochSecond(ZoneOffset.UTC)

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)