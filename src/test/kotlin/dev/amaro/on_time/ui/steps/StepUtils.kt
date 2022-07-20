package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.junit4.ComposeTestRule
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.RunCucumberTest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

interface Step {

    fun onScenarioContext(body: ComposeTestRule.() -> Unit) = runBlocking(Dispatchers.Main) {
        RunCucumberTest.composer?.run {
            body(this)
            awaitIdle()
        } ?: throw AssertionError("Compose Rule not available")
    }

    var initialState: AppState
        get() = RunCucumberTest.initialState
        set(value) {
            RunCucumberTest.initialState = value
        }
}