package dev.amaro.on_time

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.InternalTestApi
import com.appmattus.kotlinfixture.kotlinFixture
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.utilities.discardSecondsAndNanos
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Surface
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
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
    const val USER =  "some.user"
    val task1 = Task(TASK_ID_1, EMPTY, TaskState.NOT_STARTED)
    val task2 = Task(TASK_ID_2, EMPTY, TaskState.NOT_STARTED)
    val task3 = Task(TASK_ID_3, EMPTY, TaskState.NOT_STARTED)
    val configuration = Configuration(HOST, TOKEN, USER)

    val workingTask1 = asWorkingTask(task1)
    fun asWorkingTask(task: Task, startedAt: LocalDateTime = LocalDateTime.now().discardSecondsAndNanos(), minutes: Int = 0) =
        WorkingTask(task, startedAt, minutes)

}

@OptIn(InternalTestApi::class, ExperimentalTestApi::class)
fun ComposeUiTest.takeScreenshot(name: String) {
    if (this is DesktopComposeUiTest) {
        val surface: Surface = getFromField("surface")
        val imageData = surface.makeImageSnapshot().encodeToData(EncodedImageFormat.PNG)
        val buffer = ByteBuffer.wrap(imageData?.bytes)

        try {
            val path: Path = Path.of(name)
            val channel: ByteChannel = Files.newByteChannel(
                path,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
            )
            channel.write(buffer)
            channel.close()
        } catch (e: IOException) {
            println(e)
        }
    }
}
inline fun <reified T, reified R> T.getFromField(method: String) : R {
    return reflectSearch(T::class.java) { it.getDeclaredField(method) }.apply { isAccessible = true }.get(this) as R
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