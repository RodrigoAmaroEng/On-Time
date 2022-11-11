package dev.amaro.on_time.network

import dev.amaro.on_time.models.Configuration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response


// TODO: When saving the configuration, it gives an error and the screen keeps stuck on loading
class JiraRequester(private var configuration: Configuration) {

    private val client: OkHttpClient = OkHttpClient().newBuilder().build()

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

    private fun String.toJsonRequestBody(): RequestBody =
        this.toRequestBody("application/json".toMediaType())

    inline fun <reified T> get(path: String): T? {
        return Request.Builder()
            .create(path)
            .get()
            .perform()
            .body
            ?.string()
            ?.let { json.decodeFromString(it) }
    }

    fun put(path: String, body: Map<String, String>): String? {
        return put(path, json.encodeToString(body))
    }

    fun put(path: String, body: String): String? {
        val response: Response = Request.Builder()
            .create(path)
            .put(body.toJsonRequestBody())
            .perform()

        return response.body?.string()
    }

    fun post(path: String, body: Map<String, String>): String? {
        return post(path, json.encodeToString(body))
    }

    fun post(path: String, body: String): String? {
        val response: Response = Request.Builder()
            .create(path)
            .post(body.toJsonRequestBody())
            .perform()

        return response.body?.string()
    }
}