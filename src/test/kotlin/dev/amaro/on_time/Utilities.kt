package dev.amaro.on_time

import com.appmattus.kotlinfixture.kotlinFixture
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.utilities.discardSecondsAndNanos
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
    const val HOST = "https://some-host/jira/"
    const val TOKEN = "some-api-token"
    const val ONE_PROJECT = "CAT"
    const val TWO_PROJECTS = "CAT,CST"
    const val USER =  "some.user"
    val task1 = Task(TASK_ID_1, EMPTY, TaskState.NOT_STARTED)
    val task2 = Task(TASK_ID_2, EMPTY, TaskState.NOT_STARTED)
    val task3 = Task(TASK_ID_3, EMPTY, TaskState.NOT_STARTED)
    val configuration = Configuration(HOST, TOKEN, USER, TWO_PROJECTS)

    val workingTask1 = asWorkingTask(task1)
    fun asWorkingTask(task: Task, startedAt: LocalDateTime = LocalDateTime.now().discardSecondsAndNanos(), minutes: Int = 0) =
        WorkingTask(task, startedAt, minutes)

}

