package dev.amaro.on_time.network

import dev.amaro.on_time.models.Task

interface Connector {
    fun getTasks(conditions: Jql.Builder = Jql.Builder()): List<Task>

    fun assign(task: Task, userName: String)

    fun changeStatus(task: Task)
}