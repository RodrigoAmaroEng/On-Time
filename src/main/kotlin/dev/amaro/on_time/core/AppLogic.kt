package dev.amaro.on_time.core

import dev.amaro.on_time.utilities.takeIfInstance
import dev.amaro.sonic.*
import kotlin.reflect.KClass

class AppLogic(vararg middlewares: IMiddleware<AppState> ):
    StateManager<AppState>(
        AppState(),
        middlewares.asList().plus(ConditionedDirectMiddleware(
            Actions.StartTask::class,
            Actions.FilterMine::class
        ))
    ) {

    override val reducer: IReducer<AppState> = AppReducer()

    override fun reduce(action: IAction) {
        super.reduce(action)
        action.takeIfInstance<Actions>()?.sideEffect?.run {
            perform(this)
        }
    }

}

class ConditionedDirectMiddleware(
    private vararg val actions: KClass<*>
): IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (actions.contains(action::class)) {
            processor.reduce(action)
        }
    }


}