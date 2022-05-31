package dev.amaro.on_time.utilities

import java.io.InputStream
import java.util.*

object Resources {
    private fun load(path: String): InputStream? {
        return Resources::class.java.getResourceAsStream(path)
    }

    fun getConfiguration(): Properties {
        return Properties().apply {
            load(load("/local.properties"))
        }
    }
}