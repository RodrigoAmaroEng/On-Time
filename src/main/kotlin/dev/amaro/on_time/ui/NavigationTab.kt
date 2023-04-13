package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationTab(icon: String, name: String, isActive: Boolean = false, modifier: Modifier = Modifier, onSelect: () -> Unit = {}) {
    val backgroundColor = if (isActive) MaterialTheme.extension.blackBase else MaterialTheme.colors.primary
    val foregroundColor = if (isActive) MaterialTheme.extension.blackOverPrimary else MaterialTheme.colors.onPrimary
    Surface(color = backgroundColor, modifier = modifier.onClick { onSelect() }) {
        Row(
            Modifier.padding(Theme.Dimens.Spacing.LARGE).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(icon), name, tint = foregroundColor, modifier = Modifier.size(Theme.Dimens.Icons.MEDIUM))
            Spacer(Modifier.width(Theme.Dimens.Spacing.LARGE))
            Text(name, style = MaterialTheme.extension.section, color = foregroundColor)
        }
    }

}

@Composable
@Preview
fun previewNavigationTab() {
    NewOnTimeTheme {
        Column {
            NavigationTab(Icons.Tabs.MAIN, "Tasks")
            NavigationTab(Icons.Tabs.SETTINGS, "Settings", true)
        }


    }
}