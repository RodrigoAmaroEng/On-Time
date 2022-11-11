package dev.amaro.on_time.utilities

import dev.amaro.on_time.models.Configuration
import java.io.File
import java.util.*

interface ConfigurationManager {
    fun load(): Configuration
    fun save(configuration: Configuration)
}

class ConfigurationManagerImpl(
    configurationFolder: String
) : ConfigurationManager {
    init {
        File(configurationFolder).run {
            if (!exists()) mkdirs()
        }
    }
    private val configurationFile = "$configurationFolder/local.properties"

    override fun load(): Configuration {
        return Resources.loadConfigurationFile(configurationFile).run {
            Configuration(
                host = getProperty("host", ""),
                user = getProperty("user", ""),
                token = getProperty("token", "")
            )
        }
    }

    override fun save(configuration: Configuration) {
        val properties = Properties()
        properties.setProperty("host", configuration.host)
        properties.setProperty("user", configuration.user)
        properties.setProperty("token", configuration.token)
        Resources.saveConfigurationFile(configurationFile, properties)
    }

}