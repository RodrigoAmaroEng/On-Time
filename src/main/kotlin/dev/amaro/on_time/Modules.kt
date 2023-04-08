package dev.amaro.on_time

import dev.amaro.on_time.core.*
import dev.amaro.on_time.log.*
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.network.*
import dev.amaro.on_time.utilities.ConfigurationManager
import dev.amaro.on_time.utilities.ConfigurationManagerImpl
import dev.amaro.sonic.IMiddleware
import org.koin.dsl.module

object Modules {
    fun generateReleaseModule(parameters: Parameters) = module {
        single {
            buildMap {
                put(TaskState.NOT_STARTED, JiraStateDefinition(41, listOf("ToDo", "READY FOR DEVELOPMENT")))
                put(TaskState.WORKING, JiraStateDefinition(31, "IN PROGRESS"))
                put(TaskState.ON_REVIEW, JiraStateDefinition(51, "IN CODE REVIEW"))
                put(TaskState.ON_QA, JiraStateDefinition(101, listOf("READY FOR QA", "In QA")))
            }
        }

        single<ConfigurationManager> {
            val settingsFolder = parameters.resolve(FOLDER_PARAM_KEY) ?: "."
            ConfigurationManagerImpl(settingsFolder)
        }

        factory {
            val configurationManager: ConfigurationManager = get()
            configurationManager.load()
        }
        single {
            JiraRequester(get())
        }
        single<Connector> { JiraConnector(get(), JiraMapper(get(), get<Configuration>().user)) }
//        single<Connector> { VoidConnector }
        single<Storage> { SQLiteStorage() }
        single { Clock() }
        single<Logger> { TaskLogger(get(), get()) }

        factory {
            arrayOf(
                ServiceMiddleware(get()),
                StorageMiddleware(get(), get()),
                SettingsMiddleware(get())
            )
        }
        single { params -> AppLogic(
            initialState = params.get(),
            debugMode = false,
            middlewares = get<Array<IMiddleware<AppState>>>())
        }
    }
}