package dev.amaro.on_time.ui.steps

import androidx.compose.ui.test.junit4.ComposeTestRule
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.JBehaveComposeTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

interface Step {
    fun onScenarioContext(body: ComposeTestRule.() -> Unit) = runBlocking(Dispatchers.Main) {
        JBehaveComposeTest.composer?.run {
            body(this)
            awaitIdle()
        } ?: throw AssertionError("Compose Rule not available")
    }

    fun setInitialState(state: AppState) {
        JBehaveComposeTest.initialState = state
    }
}