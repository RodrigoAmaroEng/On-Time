package dev.amaro.on_time.ui.steps

import dev.amaro.on_time.Samples
import dev.amaro.on_time.network.Connector
import dev.amaro.on_time.network.Jql
import dev.amaro.on_time.network.Value
import dev.amaro.on_time.ui.JBehaveComposeTest
import io.mockk.every
import io.mockk.mockk
import org.jbehave.core.annotations.Given
import org.koin.dsl.module
import java.net.SocketException

class ContextSteps : Step {

    @Given("I have never started the App")
    fun givenIHaveNeverStartedTheApp() = onScenarioContext {
        initialState = initialState.copy(configuration = null)
    }

    @Given("I already configured the application")
    fun givenIAlreadyConfiguredTheApp() = onScenarioContext {
        initialState = initialState.copy(configuration = Samples.configuration)
    }

    @Given("there are available tasks to work")
    fun givenThereAreAvailableTasks() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(allTasksFilter) } returns listOf(Samples.task1, Samples.task2, Samples.task3)
        every { connector.getTasks(myTasksFilter) } returns listOf(Samples.task1)
        applyConnector(connector)
    }

    @Given("there are no available tasks to work")
    fun givenThereAreNoAvailableTasks() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(any()) } returns emptyList()
        applyConnector(connector)
    }

    @Given("there is no internet connection")
    fun givenThereIsNoInternetConnection() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(any()) } throws SocketException()
        applyConnector(connector)
    }

    private fun applyConnector(connector: Connector) {
        JBehaveComposeTest.debugModules.add(
            module {
                single<Connector> { connector }
            }
        )
    }

    @Given("the 'Only Assigned To Me' option is not activated")
    fun step46() {
        initialState = initialState.copy(onlyMyTasks = false)
    }

    @Given("none of the available tasks are assigned to me")
    fun step51() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(allTasksFilter) } returns listOf(Samples.task2, Samples.task3)
        every { connector.getTasks(myTasksFilter) } returns emptyList()
        applyConnector(connector)
    }

    @Given("the 'Only Assigned To Me' option is activated")
    fun step56() {
        initialState = initialState.copy(onlyMyTasks = true)
    }

    private val allTasksFilter = Jql.Builder().condition {
        assignee().any(Value.USER, Value.EMPTY)
    }

    private val myTasksFilter = Jql.Builder().condition {
        assignee().any(Value.USER)
    }
}