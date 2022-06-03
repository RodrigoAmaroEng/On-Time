package dev.amaro.on_time.utilities

import kotlin.reflect.KClass


inline fun <reified T> Any.takeIfInstance(): T? =
    if (this is T) this else null