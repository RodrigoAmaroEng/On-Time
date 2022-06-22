package dev.amaro.on_time.ui.steps

import dev.amaro.on_time.core.AppState
import org.jbehave.core.annotations.Given

class ContextStepsForJ : Step {

    @Given("I have never started the App")
    fun givenIHaveNeverStartedTheApp() = onScenarioContext{
        setInitialState(AppState())
    }

}