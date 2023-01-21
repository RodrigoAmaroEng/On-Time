package dev.amaro.on_time.unit

import assertk.assertThat
import assertk.assertions.contains
import dev.amaro.on_time.Samples
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.network.*
import kotlin.test.Test

class ConditionsBuilderTest {
    @Test
    fun `Conditions should use the projects set on the configuration`() {
        val state = AppState(configuration = Samples.configuration)
        val conditions  = ConditionsBuilder.buildFrom(state)
        assertThat(conditions.build().toString()).contains("project IN (CAT,CST)")
    }
    @Test
    fun `Conditions should use other informed filters`() {
        val condition = GenericCondition("animal", ConditionOperator.EQUAL, "dog")
        val state = AppState(configuration = Samples.configuration, filterDefinition = FilterDefinition(listOf(condition), listOf(SortField("age"))))
        val conditions  = ConditionsBuilder.buildFrom(state)
        val output = conditions.build().toString()
        assertThat(output).contains("animal = \'dog\'")
        assertThat(output).contains("ORDER BY age ASC")
    }

    @Test
    fun `Order on can be descendant`() {
        val state = AppState(configuration = Samples.configuration, filterDefinition = FilterDefinition(emptyList(), listOf(SortField("age", SortDirection.DESC))))
        val conditions  = ConditionsBuilder.buildFrom(state)
        val output = conditions.build().toString()
        assertThat(output).contains("ORDER BY age DESC")
    }

    @Test
    fun `Operation can be not equal`() {
        val condition = GenericCondition("animal", ConditionOperator.NOT_EQUAL, "dog")
        val state = AppState(configuration = Samples.configuration, filterDefinition = FilterDefinition(listOf(condition), emptyList()))
        val conditions  = ConditionsBuilder.buildFrom(state)
        val output = conditions.build().toString()
        assertThat(output).contains("animal != \'dog\'")
    }

    @Test
    fun `Operation can be set`() {
        val condition = GenericCondition("animal", ConditionOperator.SET, "dog")
        val state = AppState(configuration = Samples.configuration, filterDefinition = FilterDefinition(listOf(condition), emptyList()))
        val conditions  = ConditionsBuilder.buildFrom(state)
        val output = conditions.build().toString()
        assertThat(output).contains("animal = dog")
    }
}