package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.jbehave.core.annotations.Then

class AssertionStepsForJ : Step {

    @Then("it will show message explaining it needs to be configured")
    fun step1() = onScenarioContext {
        onNodeWithText("No configuration was found").assertExists()
    }

    @Then("show a button to start the configuration")
    fun step2()  = onScenarioContext {
        onNodeWithTag("StartConfigurationButton").assertExists()
    }

}