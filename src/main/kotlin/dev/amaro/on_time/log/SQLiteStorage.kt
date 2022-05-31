package dev.amaro.on_time.log

import androidx.compose.runtime.key
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.ZoneOffset

class SQLiteStorage : Storage {
    private val db: Connection? = try {
        val dbPath = "./build/resources/main/data/task.db"
        println("DBPath: $dbPath")
        DriverManager.getConnection("jdbc:sqlite:$dbPath")
    } catch (e: SQLException) {
        println("Connection failed!")
        null
    }

    override fun include(event: LogEvent, taskId: String, timeStamp: LocalDateTime) {
        try {
            db?.prepareStatement("INSERT INTO log (key, timestamp, kind) VALUES (?,?,?)")?.run {
                setString(1, taskId)
                setLong(2, timeStamp.toEpochSecond(ZoneOffset.UTC))
                setString(3, event.stamp.toString())
                executeUpdate()
            }
        } catch (e: Exception) {
            println("Error: $e")
        }
    }
}