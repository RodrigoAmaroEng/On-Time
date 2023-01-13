package dev.amaro.on_time.models

data class Configuration(
    val host: String = "",
    val token: String = "",
    val user: String = "",
    val projects: String = ""
) {
    val isValid: Boolean
        get() = host.isNotEmpty() && token.isNotEmpty() && user.isNotEmpty()
}