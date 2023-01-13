package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SettingItem(name: String, value: String, tag: String, onChange: (String) -> Unit) {
    Column {
        Row(
            Modifier.background(MaterialTheme.colors.background)
                .fillMaxWidth()
                .height(Theme.Dimens.Height.SMALL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.3f)
                    .height(Theme.Dimens.Height.SMALL)
                    .background(MaterialTheme.colors.surface)
                    .padding(Theme.Dimens.Margins.MEDIUM, Theme.Dimens.Margins.SMALL)
                    .testTag(tag),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    name,
                    style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold)
                )
            }
            BasicTextField(
                value,
                onChange,
                textStyle = MaterialTheme.typography.body2,
                modifier = Modifier.padding(Theme.Dimens.Margins.MEDIUM, Theme.Dimens.Margins.SMALL)
                    .fillMaxWidth()
                    .testTag("${tag}_Value")
            )
        }
        Box(Modifier.fillMaxWidth().height(Theme.Dimens.BORDER).background(MaterialTheme.colors.secondaryVariant))
    }
}

@Composable
@Preview
fun SettingItemPreview() {
    OnTimeTheme {
        Column {
            SettingItem("Host", "https://jira.com", "") {}
            SettingItem("Host", "https://jira.com", "") {}
        }
    }
}