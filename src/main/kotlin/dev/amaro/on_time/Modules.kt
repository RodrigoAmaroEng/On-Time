package dev.amaro.on_time

import dev.amaro.on_time.core.AppLogic
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.core.ServiceMiddleware
import dev.amaro.on_time.core.StorageMiddleware
import dev.amaro.on_time.log.Clock
import dev.amaro.on_time.log.SQLiteStorage
import dev.amaro.on_time.log.Storage
import dev.amaro.on_time.log.TaskLogger
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.network.*
import dev.amaro.on_time.utilities.JiraStateMap
import dev.amaro.on_time.utilities.Resources
import dev.amaro.sonic.IMiddleware
import org.koin.dsl.module
import java.util.*

object Modules {
    private const val CONFIGURATION_FILE = "/local.properties"
    val release = module {
        single<JiraStateMap> {
            buildMap {
                put(TaskState.NOT_STARTED, JiraStateDefinition(41, listOf("ToDo", "READY FOR DEVELOPMENT")))
                put(TaskState.WORKING, JiraStateDefinition(31, "IN PROGRESS"))
                put(TaskState.ON_REVIEW, JiraStateDefinition(51, "IN CODE REVIEW"))
                put(TaskState.ON_QA, JiraStateDefinition(101, listOf("READY FOR QA", "In QA")))
                put(TaskState.DONE, JiraStateDefinition(91, "Done"))
            }
        }
        factory { Resources.getConfigurationFile(CONFIGURATION_FILE) }
        factory {
            val properties: Properties = get()
            Configuration(
                properties.getProperty("host"),
                properties.getProperty("token"),
                properties.getProperty("user")
            )
        }
        single {
            get<Configuration>().let {
                JiraRequester(it.host, it.token)
            }
        }
//        single<Connector> { JiraConnector(get(), JiraMapper(get<JiraStateMap>(), get<Configuration>().user)) }
        single<Connector> { VoidConnector }
        single<Storage> { SQLiteStorage() }
        single { Clock() }
        single { TaskLogger(get(), get()) }

        factory {
            arrayOf<IMiddleware<AppState>>(
                ServiceMiddleware(get()),
                StorageMiddleware(get())
            )
        }
        single { params -> AppLogic(initialState = params.get(), false, *get<Array<IMiddleware<AppState>>>()) }
    }
}