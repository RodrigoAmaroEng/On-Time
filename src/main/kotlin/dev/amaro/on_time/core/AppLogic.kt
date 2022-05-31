package dev.amaro.on_time.core

import dev.amaro.sonic.*
import kotlin.reflect.KClass

class AppLogic(vararg middlewares: IMiddleware<AppState> ):
    StateManager<AppState>(
        AppState(),
        middlewares.asList().plus(ConditionedDirectMiddleware(Actions.StartTask::class))
    ) {

    override val reducer: IReducer<AppState> = AppReducer()

}

class ConditionedDirectMiddleware<T: Actions>(
    private vararg val actions: KClass<T>
): IMiddleware<AppState> {
    override fun process(action: IAction, state: AppState, processor: IProcessor<AppState>) {
        if (actions.contains(action::class)) {
            processor.reduce(action)
        }
    }


}