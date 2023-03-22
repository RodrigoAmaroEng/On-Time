package dev.amaro.on_time.log

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import dev.amaro.on_time.models.Task
import dev.amaro.on_time.models.TaskState
import dev.amaro.on_time.models.WorkingTask
import dev.amaro.on_time.utilities.toDatabase
import dev.amaro.on_time.utilities.toLocalDateTime
import java.io.File
import java.time.ZoneOffset

abstract class ISQLiteStorage : Storage {
    protected abstract val driver: SqlDriver
    protected val db: MyTasks by lazy {
        if (shouldCreateTables()) {
            MyTasks.Schema.create(driver)
        }
        MyTasks(driver)
    }

    protected abstract fun shouldCreateTables(): Boolean
}

open class SQLiteStorage : ISQLiteStorage() {
    companion object {
        private const val DB_NAME = "on_time.db"
    }

    override val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:$DB_NAME")

    override fun shouldCreateTables(): Boolean = !File(DB_NAME).exists()

    override fun include(storeItem: StoreItemTask) {
        when (storeItem.event) {
            LogEvent.TASK_END -> {
                db.my_tasksQueries.finishTask(
                    storeItem.minutes,
                    storeItem.task.id,
                    storeItem.timeStamp.toEpochSecond(ZoneOffset.UTC)
                )
            }
            LogEvent.TASK_START -> {
                db.my_tasksQueries.startTask(
                    storeItem.task.id,
                    storeItem.task.title,
                    storeItem.task.status.name,
                    if (storeItem.task.isMine) 1 else 0,
                    storeItem.timeStamp.toDatabase()
                )
            }
            LogEvent.POMODORO_START -> {
                db.my_tasksQueries.startPomodoro(
                    storeItem.task.id,
                    storeItem.timeStamp.toDatabase()
                )
            }
            LogEvent.POMODORO_END -> {
                db.my_tasksQueries.stopPomodoro(
                    storeItem.task.id,
                    storeItem.timeStamp.toDatabase()
                )
            }
        }
    }

    override fun getOpen(): WorkingTask? {
        return db.my_tasksQueries.selectCurrent().executeAsOneOrNull()?.let {
            WorkingTask(
                Task(
                    it.key,
                    it.title,
                    TaskState.valueOf(it.status),
                    it.is_mine > 0
                ),
                it.timestamp.toLocalDateTime(),
                it.minutes.toInt(),
                it.pomodoro_timestamp?.toLocalDateTime()
            )
        }
    }

    override fun getLastTask(): Task? {
        return db.my_tasksQueries.selectLastTask().executeAsOneOrNull()?.let {
            Task(
                it.key,
                it.title,
                TaskState.valueOf(it.status),
                it.is_mine > 0
            )
        }
    }
}

class TestSQLiteStorage : SQLiteStorage() {
    override val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    override fun shouldCreateTables(): Boolean  = true

    fun clearDatabase() {
        db.my_tasksQueries.deleteAllLogs()
    }

    fun dumpLogs() {
        db.my_tasksQueries.showAllLogs().executeAsList().forEach {
            println(it)
        }
    }

    val database = db
}