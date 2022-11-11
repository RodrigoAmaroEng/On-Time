package dev.amaro.on_time.network

import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task

interface Connector {

    fun update(configuration: Configuration)

    fun getTasks(conditions: Jql.Builder = Jql.Builder()): List<Task>

    fun assign(task: Task, userName: String)

    fun changeStatus(task: Task)
}

object VoidConnector : Connector {
    override fun update(configuration: Configuration)  = Unit

    override fun getTasks(conditions: Jql.Builder): List<Task> = emptyList()

    override fun assign(task: Task, userName: String) = Unit

    override fun changeStatus(task: Task) = Unit
}