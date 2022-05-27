package dev.amaro.on_time.log

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Clock {
    companion object{
        private const val FORMAT = "yyyy-MM-dd HH:mm"
    }

    fun now() = LocalDateTime.now()

    fun nowStr() = now().format(DateTimeFormatter.ofPattern(FORMAT))
}