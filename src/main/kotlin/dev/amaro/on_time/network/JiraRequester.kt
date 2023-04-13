package dev.amaro.on_time.network

import dev.amaro.on_time.models.Configuration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class JiraRequester(
    private var configuration: Configuration,
    private val client: OkHttpClient = OkHttpClient().newBuilder().build()
) {

    fun update(configuration: Configuration) {
        this.configuration = configuration
    }

    @JvmSynthetic
    @PublishedApi
    internal val json = Json { ignoreUnknownKeys = true }

    @JvmSynthetic
    @PublishedApi
    internal fun Request.Builder.create(url: String): Request.Builder =
        this.url("https://${configuration.host}$url").addHeader("Authorization", "Bearer ${configuration.token}")

    @JvmSynthetic
    @PublishedApi
    internal fun Request.Builder.perform(): Response =
        client.newCall(this.build()).execute()

    inline fun <reified T> get(path: String): T? {
        return Request.Builder()
            .create(path)
            .get()
            .perform()
            .body
            ?.string()
            ?.let {
                try {
                    json.decodeFromString(it)
                } catch (e: Exception) {
                    throw JiraException(json.decodeFromString(it))
                }
            }
    }

}