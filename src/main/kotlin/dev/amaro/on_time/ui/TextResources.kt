package dev.amaro.on_time.ui

object TextResources {
    const val Title = "On Time - Task Manager"
    object Actions {
        const val Dismiss = "Dismiss"
        const val StartConfiguration = "Start Configuration"
    }
    object Errors {
        const val NetworkError = "Network error"
        const val NoConfiguration = "No configuration was found"
        const val NoTasksAvailable = "No tasks are available"
        const val NotAllSettingsWereInformed = "You need to fill in all settings to proceed"
    }
    const val Loading = "Loading..."
    object Screens {
        object Settings {
            const val Title = "Main Settings"
            const val SettingHost = "Host"
            const val SettingUsername = "Username"
            const val SettingApiKey = "API Key"
        }
    }
}