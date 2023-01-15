package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.contains
import dev.amaro.on_time.Samples
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.network.ConditionsBuilder
import kotlin.test.Test

class ConditionsBuilderTest {
    @Test
    fun `Conditions should use the projects set on the configuration`() {
        val state = AppState(configuration = Samples.configuration)
        val conditions  = ConditionsBuilder.buildFrom(state)
        println(conditions.build().toString())
        assertThat(conditions.build().toString()).contains("project IN (CAT,CST)")
    }

}