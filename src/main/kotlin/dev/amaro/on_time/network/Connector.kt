package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task

interface Connector {
    fun getTasks(): List<Task>

    fun assignMe(task: Task)

}