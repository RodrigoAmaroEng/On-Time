package dev.amaro.on_time.ui.steps

import dev.amaro.on_time.Samples
import dev.amaro.on_time.log.LogEvent
import dev.amaro.on_time.log.StoreItemTask
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.network.*
import dev.amaro.on_time.ui.RunCucumberTest
import io.cucumber.java.en.Given
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.koin.dsl.module
import java.net.SocketException
import java.time.LocalDateTime

class ContextSteps : Step {

    @Given("the OnTime app environment")
    fun step0() {
        RunCucumberTest.createEnvironment()
    }

    @Given("I have never started the App")
    fun step1() {
        overrideOnDI(Configuration())
    }

    @Given("I already configured the application")
    fun step2()  {
        overrideOnDI(Samples.configuration)
    }

    @Given("there are available tasks to work")
    fun step3() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(fullFilterAllTasks) } returns listOf(Samples.task1, Samples.task2, Samples.task3)
        every { connector.getTasks(myTasksFilter) } returns listOf(Samples.task1)
        every { connector.getTasks(noProjectMyTasks) } returns listOf(Samples.task1)
        overrideOnDI(connector)
    }

    @Given("there are no available tasks to work")
    fun step4() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(any()) } returns emptyList()
        overrideOnDI(connector)
    }

    @Given("there is no internet connection")
    fun step5() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(any()) } throws SocketException()
        overrideOnDI(connector)
    }

    @Given("the 'Only Assigned To Me' option is not activated")
    fun step6() {
        initialState = initialState.copy(onlyMyTasks = false)
    }

    @Given("none of the available tasks are assigned to me")
    fun step7() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(fullFilterAllTasks) } returns listOf(Samples.task2, Samples.task3)
        every { connector.getTasks(myTasksFilter) } returns emptyList()
        overrideOnDI(connector)
    }

    @Given("the 'Only Assigned To Me' option is activated")
    fun step8() {
        initialState = initialState.copy(onlyMyTasks = true)
    }

    @Given("I have no current task")
    fun step9() {
        RunCucumberTest.storage.clearDatabase()
    }

    @Given("I have a current task")
    fun step10() {
        RunCucumberTest.storage.clearDatabase()
        RunCucumberTest.storage.include(StoreItemTask(LogEvent.TASK_START, Samples.task1, LocalDateTime.now()))
        clearMocks(RunCucumberTest.storage)
    }

    @Given("a task that is Unassigned")
    fun step11() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(fullFilterAllTasks) } returns listOf(Samples.task1)
        overrideOnDI(connector)
    }

    @Given("I have previously worked on task {word}")
    fun step12(taskId: String) {
        RunCucumberTest.storage.clearDatabase()
        RunCucumberTest.storage.include(StoreItemTask(LogEvent.TASK_START, Samples.task1.copy(id = taskId), LocalDateTime.now().minusMinutes(10)))
        RunCucumberTest.storage.include(StoreItemTask(LogEvent.TASK_END, Samples.task1.copy(id = taskId), LocalDateTime.now().minusMinutes(5)))
        clearMocks(RunCucumberTest.storage)
    }

    private inline fun <reified T> overrideOnDI(objectInstance: T) {
        RunCucumberTest.debugModules.add(
            module {
                single<T> { objectInstance }
            }
        )
    }

    private val fullFilterAllTasks = Jql.Builder().apply {
        condition {
            assignee().any(Value.USER, Value.EMPTY)
        }
        and { project().any(Value.fromScalar("CAT"), Value.fromScalar("CST")) }
        and { field("resolution").set("Unresolved") }
        and { field("Platform").set("Android") }
        orderBy("updated").desc()
    }

    private val noProjectMyTasks = Jql.Builder().apply {
        condition {
            assignee().any(Value.USER)
        }
        and { field("resolution").set("Unresolved") }
        and { field("Platform").set("Android") }
        orderBy("updated").desc()
    }

    private val myTasksFilter = Jql.Builder().apply {
        condition {
            assignee().any(Value.USER)
        }
        and { project().any(Value.fromScalar("CAT"), Value.fromScalar("CST")) }
        and { field("resolution").set("Unresolved") }
        and { field("Platform").set("Android") }
        orderBy("updated").desc()
    }


}