package dev.amaro.on_time.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.Heading
import dev.amaro.on_time.ui.SettingItem
import dev.amaro.on_time.ui.TextResources
import dev.amaro.on_time.utilities.withTag

@Composable
fun SettingsScreen(state: AppState, onAction: (Actions) -> Unit) = Screen(
    modifier = withTag("SettingsScreen"),
    toolbarContent = {},
    content = {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = withTag("MainScreen")) {
                Heading(TextResources.Screens.Settings.Title)
                SettingItem(TextResources.Screens.Settings.SettingHost, "on-time:host", "") {}
                SettingItem(TextResources.Screens.Settings.SettingUsername, "on-time:username", "") {}
                SettingItem(TextResources.Screens.Settings.SettingApiKey, "on-time:api-key", "") {}
            }
        }
    })

@Composable
@Preview
fun previewSettingsScreen() {
    SettingsScreen(AppState()) {}
}