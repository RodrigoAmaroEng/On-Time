package dev.amaro.on_time.log

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

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