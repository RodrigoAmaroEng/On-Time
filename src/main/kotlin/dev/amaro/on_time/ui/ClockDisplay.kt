package dev.amaro.on_time.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun ClockDisplay(initial: LocalDateTime) {
    fun elapsed() : String {
        return Duration.between(initial, LocalDateTime.now()).let{
            "${it.toHoursFormat()}:${it.toMinutesFormat()}"
        }
    }
    val time = remember { mutableStateOf(elapsed()) }
    LaunchedEffect(0) { // 3
        while (true) {
            time.value = elapsed()
            delay(1000)
        }
    }
    Text(time.value)
}

fun Duration.toHoursFormat(): String  {
    return toHoursPart().toString().padStart(2,'0')
}

fun Duration.toMinutesFormat(): String  {
    return toHoursPart().toString().padStart(2,'0')
}