package dev.amaro.on_time.ui.steps

import dev.amaro.on_time.Samples
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.network.Connector
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
        initialState = initialState.copy(configuration = Configuration("", "", ""))
    }

    @Given("there are available tasks to work")
    fun givenThereAreAvailableTasks() {
        initialState = initialState.copy(tasks = listOf(Samples.task1, Samples.task2, Samples.task3))
    }

    @Given("there are no available tasks to work")
    fun givenThereAreNoAvailableTasks() {
        initialState = initialState.copy(tasks = emptyList())
    }

    @Given("there is no internet connection")
    fun givenThereIsNoInternetConnection() {
        val connector: Connector = mockk(relaxed = true)
        every { connector.getTasks(any()) } throws SocketException()
        JBehaveComposeTest.debugModules.add(
            module {
                single<Connector> { connector }
            }
        )
    }

    @Given("the Only Assigned To me option is not activated")
    fun step46() {
        initialState = initialState.copy(currentTask = null)
    }

    @Given("none of the tasks are assigned to me")
    fun step51() {
        initialState = initialState.copy(tasks = listOf(Samples.task1, Samples.task2, Samples.task3))
    }

    @Given("the Only Assigned To me option is activated")
    fun step56() {
        initialState = initialState.copy(currentTask = Samples.workingTask1)
    }
}