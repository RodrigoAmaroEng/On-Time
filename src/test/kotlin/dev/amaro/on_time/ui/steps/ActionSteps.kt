@file:OptIn(ExperimentalTestApi::class)

package dev.amaro.on_time.ui.steps

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.*
import dev.amaro.on_time.Samples
import dev.amaro.on_time.ui.RunCucumberTest
import dev.amaro.on_time.ui.Tags
import dev.amaro.on_time.ui.screens.MainScreen
import io.cucumber.java.en.When


class ActionSteps : Step {

    @When("I start the App")
    @When("this task is rendered")
    fun step1() {
        RunCucumberTest.startApp { state, dispatcher -> MainScreen(state, dispatcher) }
    }

    @When("I press the Filter Assigned to Me button")
    fun step2() = onScenarioContext {
        onNodeWithTag(Tags.FilterMineButton).performClick()
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
    fun step5() = onScenarioContext {
        onNodeWithTag(Tags.StopWorkingButton).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    @When("I move the cursor over the task options")
    fun step6() = onScenarioContext {
        onNodeWithTag("Task-CST-123").apply {
            performMouseInput {
                enter(Offset(0.8f, 0.5f))
            }
        }
    }
}