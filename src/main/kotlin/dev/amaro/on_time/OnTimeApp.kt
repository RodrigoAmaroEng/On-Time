package dev.amaro.on_time

import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.models.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf

class OnTimeApp(
    startingState: AppState,
    vararg modules: org.koin.core.module.Module
) : KoinComponent {

    private val appLogic: AppLogic by inject {
        parametersOf(startingState.copy(configuration = get<Configuration>()))
    }

    private var hasStarted = false

    init {
        startKoin {
            allowOverride(true)
            modules(*modules)
        }
    }

    fun initialize() {
        if (!hasStarted)
            appLogic.perform(Actions.Refresh)
        hasStarted = true
    }

    fun kill() {
        stopKoin()
    }

    fun perform(actions: Actions) {
        appLogic.perform(actions)
    }

    fun getState(): MutableStateFlow<AppState> = appLogic.listen()
}


