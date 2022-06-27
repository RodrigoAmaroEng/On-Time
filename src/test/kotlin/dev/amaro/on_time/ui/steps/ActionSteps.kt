package dev.amaro.on_time.ui.steps

import dev.amaro.on_time.ui.JBehaveComposeTest
import dev.amaro.on_time.ui.screens.MainScreen
import org.jbehave.core.annotations.When

class ActionSteps : Step {

    @When("I start the App")
    fun step1() = onScenarioContext {
        JBehaveComposeTest.startApp { state, dispatcher -> MainScreen(state, dispatcher) }
    }

}