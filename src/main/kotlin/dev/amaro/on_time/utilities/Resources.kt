package dev.amaro.on_time.utilities

import java.io.File

import java.util.*

object Resources {


    fun loadConfigurationFile(file: String): Properties {
        return Properties().apply {
            File(file).takeIf { it.exists() }?.let { load(it.bufferedReader()) }
        }
    }
    fun saveConfigurationFile(file: String, properties: Properties) {
        return properties.store(File(file).outputStream(), null)
    }
}
