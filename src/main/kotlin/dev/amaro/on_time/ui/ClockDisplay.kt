package dev.amaro.on_time.ui

import androidx.compose.material.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun ClockDisplay(initial: LocalDateTime) {
    val startedAt = remember { derivedStateOf { initial } }
    fun elapsed(since: LocalDateTime) : String {
        return Duration.between(since, LocalDateTime.now()).let{
            "${it.toHoursFormat()}:${it.toMinutesFormat()}"
        }
    }
    val time = remember { mutableStateOf(elapsed(initial)) }
    LaunchedEffect(0) { // 3
        while (true) {
            time.value = elapsed(startedAt.value)
            delay(1000)
        }
    }
    Text(time.value)
}

fun Duration.toHoursFormat(): String  {
    return toHoursPart().toString().padStart(2,'0')
}

fun Duration.toMinutesFormat(): String  {
    return toMinutesPart().toString().padStart(2,'0')
}