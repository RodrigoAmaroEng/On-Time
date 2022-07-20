package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.amaro.on_time.Samples
import dev.amaro.on_time.ui.RunCucumberTest
import dev.amaro.on_time.ui.screens.MainScreen
import io.cucumber.java.en.When


class ActionSteps : Step {

    @When("I start the App")
    fun step1()  {
        RunCucumberTest.startApp { state, dispatcher -> MainScreen(state, dispatcher) }
    }

    @When("I press the Filter Assigned to Me button")
    fun step2() = onScenarioContext{
        onNodeWithTag("FilterMineButton").performClick()
    }

    @When("I select a task")
    fun step3() = onScenarioContext {
        onNodeWithText(Samples.TASK_ID_1).performClick()
    }

    @When("I select another task")
    fun step4() = onScenarioContext {
        onNodeWithText(Samples.TASK_ID_2).performClick()
    }

    @When("I press the task finish button")
    fun step34() = onScenarioContext {
        onNodeWithTag("StopWorkingButton").performClick()
    }
}