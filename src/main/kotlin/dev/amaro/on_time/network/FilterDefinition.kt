package dev.amaro.on_time.network

data class FilterDefinition(
    val conditions: List<GenericCondition> = emptyList(),
    val sortingRules: List<SortField>
)

data class SortField(
    val fieldName: String,
    val direction: SortDirection = SortDirection.ASC
)

enum class SortDirection {
    ASC,
    DESC
}

enum class ConditionOperator {
    SET,
    EQUAL,
    NOT_EQUAL
}
data class GenericCondition(
    val fieldName: String,
    val operator: ConditionOperator,
    val value: String
)