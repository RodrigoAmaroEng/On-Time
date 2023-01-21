package dev.amaro.on_time.utilities

import dev.amaro.on_time.network.*

object PreFilters {
    val ANDROID_UNRESOLVED = FilterDefinition(
        listOf(
            GenericCondition("resolution", ConditionOperator.SET, "Unresolved"),
            GenericCondition("Platform", ConditionOperator.SET, "Android")
        ),
        listOf(
            SortField("updated", SortDirection.DESC)
        )
    )
}