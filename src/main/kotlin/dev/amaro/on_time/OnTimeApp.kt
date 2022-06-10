package dev.amaro.on_time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class OnTimeApp : KoinComponent {

    private val appLogic by inject<AppLogic>()

    private var hasStarted = false

    init {
        startKoin {
            modules(Modules.release)
        }
    }


    fun initialize() {

        if (!hasStarted)
            appLogic.perform(Actions.Refresh)
        hasStarted = true
    }

    fun perform(actions: Actions) {
        appLogic.perform(actions)
    }

    @Composable
    fun getState(): AppState = appLogic.listen().collectAsState().value
}


