package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.amaro.on_time.Samples
import dev.amaro.on_time.ui.JBehaveComposeTest
import dev.amaro.on_time.ui.screens.MainScreen
import org.jbehave.core.annotations.When

class ActionSteps : Step {

    @When("I start the App")
    fun step1() = onScenarioContext {
        JBehaveComposeTest.startApp { state, dispatcher -> MainScreen(state, dispatcher) }
    }

    @When("I press the Filter Assigned to Me button")
    fun step15() = onScenarioContext{
        onNodeWithTag("FilterMineButton").performClick()
    }

    @When("I select a task")
    fun step22() = onScenarioContext {
        onNodeWithText(Samples.TASK_ID_1).performClick()
    }

}