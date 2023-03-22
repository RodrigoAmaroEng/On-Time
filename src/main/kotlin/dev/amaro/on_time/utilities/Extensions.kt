package dev.amaro.on_time.utilities

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import java.time.LocalDateTime
import java.time.ZoneOffset


inline fun <reified T> Any.takeIfInstance(): T? =
    if (this is T) this else null

fun LocalDateTime.toDatabase(): Long = toEpochSecond(ZoneOffset.UTC)
fun LocalDateTime.discardNanos(): LocalDateTime = withNano(0)

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)

fun withTag(tag: String): Modifier =
    Modifier.semantics { testTag = tag }