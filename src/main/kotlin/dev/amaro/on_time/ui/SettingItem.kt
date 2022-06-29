package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingItem(name: String, key: String, value: String, onChange: (String) -> Unit) {
    Column {
        Row(Modifier.background(MaterialTheme.colors.background).fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth(0.3f)
                    .background(MaterialTheme.colors.surface)
                    .padding(12.dp, 6.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(name, style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold))
            }
            Text(value, style = MaterialTheme.typography.body2, modifier = Modifier.padding(12.dp, 6.dp))
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colors.secondaryVariant))
    }
}

@Composable
@Preview
fun SettingItemPreview() {
    OnTimeTheme {
        Column {
            SettingItem("Host", "on-time:host", "https://jira.com") {}
            SettingItem("Host", "on-time:host", "https://jira.com") {}
        }
    }
}