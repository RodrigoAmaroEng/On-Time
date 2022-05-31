package dev.amaro.on_time

import com.appmattus.kotlinfixture.kotlinFixture
import dev.amaro.on_time.core.Actions
import kotlin.reflect.KClass

val fixture = kotlinFixture()

 fun <T: Actions> listActions(filter: List<KClass<T>> = listOf()) = Actions::class
    .sealedSubclasses
    .filter { it !in filter }
    .map { fixture.create(it.java) as Actions }

fun Int.withZeros(n: Int) = this.toString().padStart(n, '0')