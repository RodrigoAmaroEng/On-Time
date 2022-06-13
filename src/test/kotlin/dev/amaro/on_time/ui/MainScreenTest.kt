package dev.amaro.on_time.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.amaro.on_time.ui.screens.MainScreen
import org.junit.Rule
import org.junit.Test

class MainScreenTest : ComposeTest() {

    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `Start the App`() {
        ui(compose) {
            // We need to do this because ::MainScreen is prohibited on compose functions
            setScreen { state, performer -> MainScreen(state, performer) }
            waitForIdle()
            onNodeWithTag("MainScreen").assertExists()
            awaitIdle()
        }
    }
}



