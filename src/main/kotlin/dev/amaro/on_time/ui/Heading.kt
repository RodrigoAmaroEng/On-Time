package dev.amaro.on_time.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Heading(title: String) {
    Box(Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondaryVariant)
        .padding(Theme.Dimens.Margins.MEDIUM, Theme.Dimens.Margins.SMALL)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.background,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
@Preview
fun HeadingPreview() {
    OnTimeTheme {
        Heading("Main Settings")
    }
}