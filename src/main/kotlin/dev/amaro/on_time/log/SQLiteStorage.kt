package dev.amaro.on_time.log

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import dev.amaro.on_time.models.Task
import java.time.ZoneOffset

abstract class ISQLiteStorage: Storage {
    protected abstract val driver: SqlDriver
    protected val db: MyTasks by lazy { MyTasks(driver) }
}

open class SQLiteStorage: ISQLiteStorage() {
    override val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:build/resources/main/data/my-tasks.db")

    override fun include(storeItem: StoreItemTask) {
        if (storeItem.event == LogEvent.TASK_END) {
            db.my_tasksQueries.finishTask(
                storeItem.minutes,
                storeItem.taskId,
                storeItem.timeStamp.toEpochSecond(ZoneOffset.UTC)
            )
        } else {
            db.my_tasksQueries.startTask(
                storeItem.taskId,
                storeItem.timeStamp.toEpochSecond(ZoneOffset.UTC)
            )
        }
    }

    override fun getOpen(): Task? {
        return TODO()
//        db.my_tasksQueries.selectCurrent().executeAsOne()?.let {
//            return Task(
//                it.key,
//                it.name,
//                it.start_time,
//                it.end_time
//            )
//        }
    }
}

class TestSQLiteStorage : SQLiteStorage() {
    override val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)

    val database = db

    init {
        MyTasks.Schema.create(driver)
    }

}