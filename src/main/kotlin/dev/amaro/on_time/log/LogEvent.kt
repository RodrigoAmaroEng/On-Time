package dev.amaro.on_time.log

enum class LogEvent(val stamp: Char) {
    TASK_START('S'),
    TASK_END('E');
}