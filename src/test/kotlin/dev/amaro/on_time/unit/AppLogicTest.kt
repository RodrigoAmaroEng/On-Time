package dev.amaro.on_time.unit

import dev.amaro.on_time.core.*
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test

class AppLogicTest {



    @Test
    fun `Actions that are directly processed`() {
        val reducer = mockk<AppReducer>(relaxed = true)
        val app = AppLogic(AppState(), reducer = reducer)
        val directlyProcessedActions = listOf(
            Actions.FilterMine,
            Actions.UpdateLastResult(Results.Processing),
            Actions.Navigation.GoToSettings,
            Actions.Navigation.GoToMain,
            Actions.DismissFeedback
        )
        directlyProcessedActions.forEach { app.perform(it) }
        verify(exactly = directlyProcessedActions.size) {
            reducer.reduce(any(), any())
        }
    }

}