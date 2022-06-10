package dev.amaro.on_time.utilities

import java.io.InputStream
import java.util.*

object Resources {
    private fun load(path: String): InputStream? {
        return Resources::class.java.getResourceAsStream(path)
    }

    fun getConfigurationFile(file: String): Properties {
        return Properties().apply {
            load(load(file))
        }
    }
}