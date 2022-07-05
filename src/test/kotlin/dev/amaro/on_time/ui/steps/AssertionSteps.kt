package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import dev.amaro.on_time.Samples
import org.jbehave.core.annotations.Then

class AssertionSteps : Step {

    @Then("it will show message explaining it needs to be configured")
    fun step1() = onScenarioContext {
        onNodeWithText("No configuration was found").assertExists()
    }

    @Then("show a button to start the configuration")
    fun step2() = onScenarioContext {
        onNodeWithTag("StartConfigurationButton").assertExists()
    }

    @Then("it will display the list of tasks available")
    fun step3() = onScenarioContext {
        onNodeWithTag("QueryResults").assertExists()
    }

    @Then("the filter options will be visible")
    fun step4() = onScenarioContext {
        onNodeWithTag("TaskFilters").assertExists()
    }

    @Then("it will present a message telling there are no tasks available")
    fun step5() = onScenarioContext {
        println(" --> Asserting no tasks")
        onNodeWithTag("NoTasksAvailableMessage").assertExists()
    }

    @Then("it will present a message informing it wasn't able to retrieve the tasks")
    fun step6() = onScenarioContext {
        onNodeWithTag("NetworkFailureMessage").assertExists()
    }

    @Then("it will show only my task")
    fun step7() = onScenarioContext {
        onNodeWithTag("QueryResults").assertExists()
        onNodeWithText(Samples.TASK_ID_1).assertExists()
        onNodeWithText(Samples.TASK_ID_2).assertDoesNotExist()
        onNodeWithText(Samples.TASK_ID_3).assertDoesNotExist()
    }

    @Then("it will show all tasks")
    fun step8() = onScenarioContext {
        onNodeWithTag("QueryResults").assertExists()
        onNodeWithText(Samples.TASK_ID_1).assertExists()
        onNodeWithText(Samples.TASK_ID_2).assertExists()
        onNodeWithText(Samples.TASK_ID_3).assertExists()
    }

}