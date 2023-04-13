package dev.amaro.on_time.network

import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.models.Task

interface Connector {

    fun update(configuration: Configuration)

    fun getTasks(conditions: Jql.Builder = Jql.Builder()): List<Task>

}

