package dev.amaro.on_time.log

import dev.amaro.on_time.utilities.discardSecondsAndNanos
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Clock(private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
    companion object {
        private const val FORMAT = "yyyy-MM-dd HH:mm"
    }
    private var timerJob: Job? = null

    fun now(): LocalDateTime = LocalDateTime.now().discardSecondsAndNanos()

    fun nowStr(): String = now().format(DateTimeFormatter.ofPattern(FORMAT))

    fun startTimer(minutes: Long, callback: () -> Unit) {
        timerJob?.cancel()
        timerJob = scope.launch {
            delay(minutes * 60 * 1000)
            callback()
        }
    }
    fun stopTimer() {
        timerJob?.cancel()
    }
}