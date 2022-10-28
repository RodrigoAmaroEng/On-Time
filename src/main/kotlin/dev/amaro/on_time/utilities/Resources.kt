package dev.amaro.on_time.utilities

import java.io.File
import java.io.InputStream
import java.util.*

object Resources {
    private val basePath = File(".").canonicalPath
    private fun load(file: String): InputStream {
        if (!File(basePath, file).exists())
            File(Resources::class.java.getResource(file)!!.file).copyTo(File(basePath, file))
        return File(basePath, file).inputStream()
    }

    fun loadConfigurationFile(file: String): Properties {
        return Properties().apply {
            load(load(file))
        }
    }
    fun saveConfigurationFile(file: String, properties: Properties) {
        return properties.store(File(basePath, file).outputStream(), null)
    }
}
