package dev.amaro.on_time.core

import dev.amaro.on_time.utilities.takeIfInstance
import dev.amaro.sonic.*

class AppLogic(
    initialState: AppState = AppState(),
    private val debugMode: Boolean = false,
    override val reducer: IReducer<AppState> = AppReducer(debugMode),
    vararg middlewares: IMiddleware<AppState>
) :
    StateManager<AppState>(
        initialState,
        middlewares.asList().plus(
            ConditionedDirectMiddleware(
                Actions.FilterMine::class,
                Actions.UpdateLastResult::class,
                Actions.Navigation.GoToSettings::class,
                Actions.Navigation.GoToMain::class,
                Actions.DismissFeedback::class,
                Actions.SaveConfiguration::class
            )
        )
    ) {



    override fun reduce(action: IAction) {
        super.reduce(action)
        var nextAction = action.takeIfInstance<ISideEffectAction>()?.sideEffect
        while(nextAction != null) {
            perform(nextAction)
            nextAction = nextAction.takeIfInstance<ISideEffectAction>()?.sideEffect
        }
    }

    override fun perform(action: IAction) {
        if (debugMode)
            println(" # Action to Perform: $action")
        super.perform(action)
    }

}