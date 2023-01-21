package dev.amaro.on_time.network


import dev.amaro.on_time.network.Jql.Companion.SPACE
import dev.amaro.on_time.utilities.takeIfInstance
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun withJql(ctx: Jql.Builder.() -> Unit) = Jql.Builder().apply(ctx).build()

open class Jql private constructor(
    protected val conditions: Collection<Condition>,
    protected var orderBy: MutableList<Field>?
) {
    companion object {
        const val SPACE = ' '
    }

    class Builder() {
        protected val conditions = mutableListOf<Condition>()
        protected var orderBy: MutableList<Field>? = null

        fun condition(ctx: Condition.Builder.() -> Unit) = apply {
            conditions += Condition.Builder().apply(ctx).build()
        }

        fun and(ctx: Condition.Builder.() -> Unit) = apply {
            conditions += Condition.Builder(ConditionType.AND).apply(ctx).build()
        }

        fun or(ctx: Condition.Builder.() -> Unit) = apply {
            conditions += Condition.Builder(ConditionType.OR).apply(ctx).build()
        }

        fun orderBy(orderBy: String): Field {
            if (this.orderBy == null) {
                this.orderBy = mutableListOf()
            }
            val field = Field.fromString(orderBy)
            this.orderBy!!.add(field)
            return field
        }

        fun Field.from (order: SortDirection) {
            orderDir = when(order) {
                SortDirection.DESC -> OrderDir.DESC
                SortDirection.ASC -> OrderDir.ASC
            }
        }

        fun Field.desc() {
            this.orderDir = OrderDir.DESC
        }

        fun Field.asc() {
            this.orderDir = OrderDir.ASC
        }

        fun build() = Jql(conditions, orderBy)

        override fun equals(other: Any?): Boolean {
            return (other?.takeIfInstance<Builder>()?.build()?.queryString() == build().queryString())
        }
    }

    fun queryString(space: Char = SPACE, encode: Boolean = false): String {
        if (conditions.isEmpty()) return "jql="

        var str = conditions.joinToString("$space") { it.queryString(space) }

        if (orderBy != null) {
            str += "$space${ConditionType.ORDER_BY.value(space)}$space"
            str += orderBy!!.joinToString("%2C$space") { "${it.value}$space${it.orderDir}" }
        }
        return "jql=" + str.let {
            if (encode) it.replace("=", "%3D").replace("'", "%22") else it
        }
    }

    override fun toString() = queryString()
}

open class Condition private constructor(
    protected var field: Field? = null,
    protected var operator: Operator? = null,
    protected var value: Value? = null,
    protected val conditions: Collection<Condition>? = null,
    protected val type: ConditionType? = null
) {

    class Builder(private val type: ConditionType? = null) {
        protected var field: Field? = null
        protected var operator: Operator? = null
        protected var value: Value? = null
        protected var conditions: MutableCollection<Condition>? = null

        fun condition(ctx: Builder.() -> Unit) = apply {
            if (conditions == null)
                conditions = mutableListOf()

            conditions!!.plusAssign(Builder().apply(ctx).build())
        }

        fun and(ctx: Builder.() -> Unit) = apply {
            if (conditions == null)
                conditions = mutableListOf()

            conditions!!.plusAssign(Builder(ConditionType.AND).apply(ctx).build())
        }

        fun or(ctx: Builder.() -> Unit) = apply {
            if (conditions == null)
                conditions = mutableListOf()

            conditions!!.plusAssign(Builder(ConditionType.OR).apply(ctx).build())
        }

        fun field(field: String): Field {
            this.field = Field.fromString(field)
            return this.field!!
        }

        fun field(field: Field): Field {
            this.field = field
            return this.field!!
        }

        fun customField(fieldId: Int) = field(Field.custom(fieldId))
        fun project() = field(Field.PROJECT)
        fun status() = field(Field.STATUS)
        fun created() = field(Field.CREATED)
        fun updated() = field(Field.UPDATED)
        fun assignee() = field(Field.ASSIGNEE)
        fun reporter() = field(Field.REPORTER)
        fun issueType() = field(Field.ISSUE_TYPE)
        fun priority() = field(Field.PRIORITY)

        private fun expression(operator: Operator, value: Value) {
            this.operator = operator
            this.value = value
        }

        fun Field.user() {
            expression(Operator.EQUALS, Value.USER)
        }

        fun Field.empty() {
            expression(Operator.EQUALS, Value.EMPTY)
        }

        fun Field.notEmpty() {
            expression(Operator.NOT_EQUALS, Value.EMPTY)
        }

        fun Field.isEmpty() {
            expression(Operator.IS, Value.EMPTY)
        }

        fun Field.isNotEmpty() {
            expression(Operator.IS_NOT, Value.EMPTY)
        }

        fun Field.`null`() {
            expression(Operator.EQUALS, Value.NULL)
        }

        fun Field.notNull() {
            expression(Operator.NOT_EQUALS, Value.NULL)
        }

        fun Field.isNull() {
            expression(Operator.IS, Value.NULL)
        }

        fun Field.isNotNull() {
            expression(Operator.IS_NOT, Value.NULL)
        }

        fun Field.set(value: String) {
            expression(Operator.EQUALS, Value.fromScalar(value))
        }

        fun Field.from (operator: ConditionOperator, value: String) {
            when(operator) {
                ConditionOperator.SET -> set(value)
                ConditionOperator.EQUAL -> eq(value)
                ConditionOperator.NOT_EQUAL -> notEq(value)
            }
        }

        fun Field.eq(value: String) {
            expression(Operator.EQUALS, Value.fromString(value))
        }

        fun Field.eq(date: LocalDate) {
            expression(Operator.EQUALS, Value.fromDate(date))
        }

        fun Field.eq(dateTime: LocalDateTime) {
            expression(Operator.EQUALS, Value.fromDateTime(dateTime))
        }

        fun Field.notEq(value: String) {
            expression(Operator.NOT_EQUALS, Value.fromString(value))
        }

        fun Field.notEq(date: LocalDate) {
            expression(Operator.NOT_EQUALS, Value.fromDate(date))
        }

        fun Field.notEq(dateTime: LocalDateTime) {
            expression(Operator.NOT_EQUALS, Value.fromDateTime(dateTime))
        }

        fun Field.any(vararg value: String) {
            expression(Operator.IN, Value.fromArray(value.map { Value.fromScalar(it) }))
        }

        fun Field.any(vararg value: Value) {
            expression(Operator.IN, Value.fromArray(value.toList()))
        }

        fun Field.`in`(array: Collection<String>) {
            expression(Operator.IN, Value.fromArray(array.map { Value.fromString(it) }))
        }

        fun Field.`in`(vararg value: String) {
            expression(Operator.IN, Value.fromArray(value.map { Value.fromString(it) }))
        }

        fun Field.notIn(array: Collection<String>) {
            expression(Operator.NOT_IN, Value.fromArray(array.map { Value.fromString(it) }))
        }

        fun Field.notIn(vararg value: String) {
            expression(Operator.NOT_IN, Value.fromArray(value.map { Value.fromString(it) }))
        }

        fun Field.changedAfter(date: LocalDate) {
            expression(Operator.CHANGED_AFTER, Value.fromDate(date))
        }

        fun Field.changedAfter(dateTime: LocalDateTime) {
            expression(Operator.CHANGED_AFTER, Value.fromDateTime(dateTime))
        }

        fun Field.more(value: String) {
            expression(Operator.MORE, Value.fromString(value))
        }

        fun Field.more(date: LocalDate) {
            expression(Operator.MORE, Value.fromDate(date))
        }

        fun Field.more(dateTime: LocalDateTime) {
            expression(Operator.MORE, Value.fromDateTime(dateTime))
        }

        fun Field.moreEq(value: String) {
            expression(Operator.MORE_OR_EQUAL, Value.fromString(value))
        }

        fun Field.moreEq(date: LocalDate) {
            expression(Operator.MORE_OR_EQUAL, Value.fromDate(date))
        }

        fun Field.moreEq(dateTime: LocalDateTime) {
            expression(Operator.MORE_OR_EQUAL, Value.fromDateTime(dateTime))
        }

        fun Field.less(value: String) {
            expression(Operator.LESS, Value.fromString(value))
        }

        fun Field.less(date: LocalDate) {
            expression(Operator.LESS, Value.fromDate(date))
        }

        fun Field.less(dateTime: LocalDateTime) {
            expression(Operator.LESS, Value.fromDateTime(dateTime))
        }

        fun Field.lessEq(value: String) {
            expression(Operator.LESS_THAN_OR_EQUAL, Value.fromString(value))
        }

        fun Field.lessEq(date: LocalDate) {
            expression(Operator.LESS_THAN_OR_EQUAL, Value.fromDate(date))
        }

        fun Field.lessEq(dateTime: LocalDateTime) {
            expression(Operator.LESS_THAN_OR_EQUAL, Value.fromDateTime(dateTime))
        }

        fun build(): Condition {
            if (conditions.isNullOrEmpty()) {
                return Condition(
                    field.validate("Field"),
                    operator.validate("Operator"),
                    value.validate("Value"),
                    type = type
                )
            }
            return Condition(type = type, conditions = conditions)
        }

        private fun <T> T?.validate(type: String) = this ?: throw IllegalStateException("$type cannot be null")
    }

    fun queryString(space: Char = SPACE): String {
        return when {
            !conditions.isNullOrEmpty() -> "${type.toQuery(space)}(${
                conditions.joinToString("$space") {
                    it.queryString(
                        space
                    )
                }
            })"
            else -> "${type.toQuery(space)}${field.getOrThrow()}$space${operator.getOrThrow(space)}$space${value.getOrThrow()}"
        }
    }

    override fun toString() = queryString()

    private fun Field?.getOrThrow() = this?.value ?: throw IllegalStateException("Field cannot be null")

    private fun Operator?.getOrThrow(space: Char = SPACE) =
        this?.value(space) ?: throw IllegalStateException("Operator cannot be null")

    private fun Value?.getOrThrow() = this?.value ?: throw IllegalStateException("Value cannot be null")

    enum class Operator(override val value: String) : Instruction {
        EQUALS("="),
        NOT_EQUALS("!="),
        IS_NOT("IS NOT"),
        IS("IS"),
        NOT_IN("NOT IN"),
        IN("IN"),
        CHANGED_AFTER("CHANGED AFTER"),
        MORE(">"),
        MORE_OR_EQUAL(">="),
        LESS("<"),
        LESS_THAN_OR_EQUAL("<=")
    }
}

enum class OrderDir(override val value: String) : Instruction {
    ASC("ASC"),
    DESC("DESC")
}

enum class ConditionType(override val value: String) : Instruction {
    AND("AND"),
    OR("OR"),
    ORDER_BY("ORDER BY");
}

interface Instruction {
    val value: String
    fun value(space: Char = SPACE) = if (space == SPACE) value else value.replace(SPACE, space)
}

fun ConditionType?.toQuery(space: Char = SPACE) = this?.value?.let { "$it$space" } ?: ""

open class Field {
    lateinit var value: String
    var orderDir: OrderDir? = null

    companion object {
        val PROJECT = fromString("project")
        val STATUS = fromString("status")
        val CREATED = fromString("created")
        val UPDATED = fromString("updated")
        val ASSIGNEE = fromString("assignee")
        val REPORTER = fromString("reporter")
        val ISSUE_TYPE = fromString("issuetype")
        val PRIORITY = fromString("priority")

        fun fromString(str: String) = Field().apply { value = str }
        fun custom(id: Int) = Field().apply { value = "cf[$id]" }
    }
}

open class Value {
    lateinit var value: String

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")!!
        private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")!!

        val EMPTY = Value().apply { value = "EMPTY" }
        val NULL = Value().apply { value = "NULL" }
        val USER = Value().apply { value = "currentUser()" }

        fun fromScalar(str: String) = Value().apply { value = str }
        fun fromString(str: String) = Value().apply { value = "'$str'" }
        fun fromDate(date: LocalDate) = Value().apply { value = "'${date.format(DATE_FORMATTER)}'" }
        fun fromDateTime(dateTime: LocalDateTime) = Value().apply { value = "'${dateTime.format(DATETIME_FORMATTER)}'" }
        fun fromArray(array: Collection<Value>) = Value().apply { value = "(${array.joinToString(",") { it.value }})" }
    }
}