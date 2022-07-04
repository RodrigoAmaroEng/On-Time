package dev.amaro.on_time

import com.appmattus.kotlinfixture.kotlinFixture
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.network.Connector
import dev.amaro.on_time.network.Jql
import dev.amaro.on_time.utilities.discardSecondsAndNanos
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.time.LocalDateTime
import kotlin.reflect.KClass

val fixture = kotlinFixture()

 fun listActions(filter: List<KClass<*>> = listOf()) = Actions::class
    .sealedSubclasses
    .filter { it !in filter }
    .map { fixture.create(it.java) as Actions }

fun Int.withZeros(n: Int) = this.toString().padStart(n, '0')

object Samples {
    val STATUS = TaskState.NOT_STARTED.name
    const val TASK_ID_1 = "CST-123"
    const val TASK_ID_2 = "CST-321"
    const val TASK_ID_3 = "CST-456"
    const val KIND_START = "S"
    const val KIND_END = "F"
    const val EMPTY = ""
    const val TRUE = 1L
    const val FALSE = 0L
    val task1 = Task(TASK_ID_1, EMPTY, TaskState.NOT_STARTED)
    val task2 = Task(TASK_ID_2, EMPTY, TaskState.NOT_STARTED)
    val task3 = Task(TASK_ID_3, EMPTY, TaskState.NOT_STARTED)

    val workingTask1 = asWorkingTask(task1)
    fun asWorkingTask(task: Task, startedAt: LocalDateTime = LocalDateTime.now().discardSecondsAndNanos(), minutes: Int = 0) =
        WorkingTask(task, startedAt, minutes)
}

inline fun <reified T> instance(vararg arguments: Any?) : T {
    val clazz: Class<T> = T::class.java
    return clazz.constructors.first()
        .apply { isAccessible = true }
        .newInstance(*arguments)
        .cast()
}

inline fun <reified T> instance(name: String, vararg arguments: Any?) : T {
    val module = Thread.currentThread().contextClassLoader.unnamedModule
    val clazz = Class.forName(name)
    clazz.module.addOpens("org.jbehave.core", module)




    val obj = clazz.constructors.first().apply { isAccessible = true }.newInstance(*arguments)
    return obj.cast()
}



inline fun <reified T> T.onMethod(method: String) : Method {
    return reflectSearch(T::class.java) { it.getDeclaredMethod(method) }.apply { isAccessible = true }
}

inline fun <reified T> T.onField(method: String) : Field {
    return reflectSearch(T::class.java) { it.getDeclaredField(method) }.apply { isAccessible = true }
}

fun <T> reflectSearch(rootClazz: Class<*>, operation: (Class<*>) -> T): T {
    var clazz : Class<*>? = rootClazz
    while (clazz != null) {
        try {
            return operation(clazz)
        } catch (e: NoSuchFieldException) {
            clazz = clazz.superclass
        }
    }
    throw NoSuchFieldException("Field d not found in class ${rootClazz.name}")
}

inline fun <reified T> Any.cast() : T = this as T


class ReportConnector : Connector {
    override fun getTasks(conditions: Jql.Builder): List<Task> {
        println("##### GetTasks:  ${conditions.build().queryString()}")
        return emptyList()
    }

    override fun assign(task: Task, userName: String) {
        println("##### Assign $task to '$userName")
    }

    override fun changeStatus(task: Task) {
        println("##### Change status $task")
    }

}