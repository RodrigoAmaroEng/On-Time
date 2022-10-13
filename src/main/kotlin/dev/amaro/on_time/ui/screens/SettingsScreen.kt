package dev.amaro.on_time.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.ui.*
import dev.amaro.on_time.utilities.withTag

@Composable
fun SettingsScreen(state: AppState, onAction: (Actions) -> Unit) = Screen(
    modifier = withTag(Tags.SettingsScreen),
    toolbarContent = {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            SquareButton(Icons.SAVE)
        }
    },
    content = {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = withTag(Tags.MainScreen)) {
                Heading(TextResources.Screens.Settings.Title)
                SettingItem(TextResources.Screens.Settings.SettingHost, "on-time:host", state.configuration?.host ?: "") {}
                SettingItem(TextResources.Screens.Settings.SettingUsername, "on-time:username", state.configuration?.user ?: "") {}
                SettingItem(TextResources.Screens.Settings.SettingApiKey, "on-time:api-key", state.configuration?.token ?: "") {}
            }
        }
    })

@Composable
@Preview
fun previewSettingsScreen() {
    SettingsScreen(AppState()) {}
}