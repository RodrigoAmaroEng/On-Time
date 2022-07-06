package dev.amaro.on_time.core

import dev.amaro.on_time.utilities.takeIfInstance
import dev.amaro.sonic.*

class AppLogic(
    initialState: AppState = AppState(),
    private val debugMode: Boolean = false,
    vararg middlewares: IMiddleware<AppState>
) :
    StateManager<AppState>(
        initialState,
        middlewares.asList().plus(
            ConditionedDirectMiddleware(
                Actions.FilterMine::class,
                Actions.UpdateLastResult::class
            )
        )
    ) {

    override val reducer: IReducer<AppState> = AppReducer(debugMode)

    override fun reduce(action: IAction) {
        super.reduce(action)
        action.takeIfInstance<ISideEffectAction>()?.sideEffect?.run {
            perform(this)
        }
    }

    override fun perform(action: IAction) {
        if (debugMode)
            println(" # Action to Perform: $action")
        super.perform(action)
    }

}