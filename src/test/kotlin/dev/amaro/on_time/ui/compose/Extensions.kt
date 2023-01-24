package dev.amaro.on_time.ui.compose

import androidx.compose.ui.ComposeScene
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.DesktopComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import dev.amaro.on_time.ui.spice.getFromField
import dev.amaro.on_time.ui.spice.getMethod
import dev.amaro.on_time.ui.spice.setField
import io.cucumber.plugin.event.PickleStepTestStep
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Surface
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.createDirectories
import kotlin.io.path.exists


@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.takeScreenshot(testData: TestData?) {
    if (testData != null)
        takeScreenshot(testData.name.normalizeName(), testData.step.normalizeName())
}
private fun String.normalizeName() = lowercase().replace(' ', '-')
@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.takeScreenshot(vararg filePath: String) {
    if (this is DesktopComposeUiTest) {
        val surface: Surface = getFromField("surface") as Surface
        val imageData = surface.makeImageSnapshot().encodeToData(EncodedImageFormat.PNG)
        val buffer = ByteBuffer.wrap(imageData?.bytes)
        val setup = arrayOf("reports", "screenshots") +
                filePath.mapIndexed { index, s -> if (index == filePath.lastIndex) "$s.png" else s }
        try {
            val path: Path = Path.of("build", *setup)
            if (!path.exists()) path.parent.createDirectories()
            val channel: ByteChannel = Files.newByteChannel(
                path,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
            )
            channel.write(buffer)
            channel.close()
        } catch (e: IOException) {
            println("Screenshot FAILED: ${e.message}")
        }
    }
}

data class TestData(val name: String, var steps: List<PickleStepTestStep>) {
    private var _step: Int = 0
    fun nextStep() = _step++

    val step: String
        get() {
            val keyword = steps[_step].step.keyword
            val text = steps[_step].step.text
            return "$_step- $keyword$text"
        }

}


@OptIn(ExperimentalTestApi::class)
fun DesktopComposeUiTest.start() {
    val createUi = getMethod("createUi")
    setField("scene", runOnUiThread { createUi.invoke(this@start) as ComposeScene })
}

@OptIn(ExperimentalTestApi::class)
fun DesktopComposeUiTest.stop() {
    val testScope: TestScope = getFromField("testScope") as TestScope
    val uncaughtExceptionHandler = getFromField("uncaughtExceptionHandler")
    val throwUncaught = uncaughtExceptionHandler.getMethod("throwUncaught")
    testScope.runTest { }
    runOnUiThread(scene::close)
    throwUncaught.invoke(uncaughtExceptionHandler)
}