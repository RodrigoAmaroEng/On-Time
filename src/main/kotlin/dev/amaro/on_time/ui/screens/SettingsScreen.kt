package dev.amaro.on_time.ui.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.amaro.on_time.core.Actions
import dev.amaro.on_time.core.AppState
import dev.amaro.on_time.models.Configuration
import dev.amaro.on_time.ui.*
import dev.amaro.on_time.utilities.withTag

@Composable
fun SettingsScreen(state: AppState, onAction: (Actions) -> Unit) {
    val configuration = remember { mutableStateOf(state.configuration ?: Configuration()) }
    return Screen(
        state,
        modifier = withTag(Tags.SettingsScreen),
        toolbarContent = {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                SquareButton(
                    Icons.SAVE,
                    modifier = Modifier.testTag(Tags.SaveButton),
                    onClick = { onAction(Actions.SaveConfiguration(configuration.value)) }
                )
            }
        },
        content = {
            Surface(color = MaterialTheme.colors.background) {
                Column(modifier = withTag(Tags.MainScreen)) {
                    Heading(TextResources.Screens.Settings.Title)
                    SettingItem(
                        TextResources.Screens.Settings.SettingHost,
                        configuration.value.host,
                        Tags.Settings_Prop_Host
                    ) {
                        configuration.value.run { configuration.value = copy(host = it) }
                    }
                    SettingItem(
                        TextResources.Screens.Settings.SettingUsername,
                        configuration.value.user,
                        Tags.Settings_Prop_User
                    ) {
                        configuration.value.run { configuration.value = copy(user = it) }
                    }
                    SettingItem(
                        TextResources.Screens.Settings.SettingApiKey,
                        configuration.value.token,
                        Tags.Settings_Prop_Token
                    ) {
                        configuration.value.run { configuration.value = copy(token = it) }
                    }
                }
            }
        })
}

@Composable
@Preview
fun previewSettingsScreen() {
    SettingsScreen(AppState()) {}
}