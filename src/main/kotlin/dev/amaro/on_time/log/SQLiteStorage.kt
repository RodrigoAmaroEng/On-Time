package dev.amaro.on_time.log

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.time.LocalDateTime
import java.time.ZoneOffset

class SQLiteStorage : Storage {
    private val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    private val db = MyTasks(driver)
    init {
        MyTasks.Schema.create(driver)
    }

    override fun include(event: LogEvent, taskId: String, timeStamp: LocalDateTime) {
        db.transaction {
            db.my_tasksQueries.insert(taskId, timeStamp.toEpochSecond(ZoneOffset.UTC), event.stamp.toString())
        }
    }
}